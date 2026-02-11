import { useState, useEffect, useCallback } from 'react';
import { CryptoAsset, WebSocketStatus, SortField, SortDirection } from '@/types/crypto';
import { apiService, CryptoPrice } from '@/services/api';
import { mockCryptoData } from '@/data/mockCryptoData';

// Convert API data to frontend format
const convertApiDataToAsset = (apiData: CryptoPrice): CryptoAsset => ({
  id: apiData.id,
  rank: apiData.market_cap_rank?.toString() || '0',
  symbol: apiData.symbol.toUpperCase(),
  name: apiData.name,
  supply: apiData.circulating_supply || 0,
  maxSupply: apiData.max_supply || null,
  marketCapUsd: apiData.market_cap || 0,
  volumeUsd24Hr: apiData.total_volume || 0,
  priceUsd: apiData.current_price || 0,
  changePercent24Hr: apiData.price_change_percentage_24h || 0,
  vwap24Hr: apiData.current_price || 0,
  explorer: '',
  sparkline: apiData.sparkline_in_7d?.price || Array(24).fill(apiData.current_price || 0),
});

export const useCryptoData = () => {
  const [cryptoData, setCryptoData] = useState<CryptoAsset[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [wsStatus, setWsStatus] = useState<WebSocketStatus>({
    connected: false,
    connecting: true,
    lastUpdate: null,
  });
  const [updatedIds, setUpdatedIds] = useState<Set<string>>(new Set());
  const [priceChanges, setPriceChanges] = useState<Record<string, 'up' | 'down' | null>>({});

  // Fetch initial data
  const fetchCryptoData = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);
      
      const response = await apiService.getCryptoPrices();
      
      if (response.success && response.data) {
        const convertedData = response.data.map(convertApiDataToAsset);
        setCryptoData(convertedData);
        setWsStatus({
          connected: true,
          connecting: false,
          lastUpdate: new Date(),
        });
      } else {
        throw new Error(response.message || 'Failed to fetch crypto data');
      }
    } catch (err) {
      console.error('Error fetching crypto data:', err);
      setError(err instanceof Error ? err.message : 'Failed to fetch data');
      // Fallback to mock data
      setCryptoData(mockCryptoData);
      setWsStatus({
        connected: false,
        connecting: false,
        lastUpdate: null,
      });
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCryptoData();
  }, [fetchCryptoData]);

  // Auto-refresh data every 30 seconds
  useEffect(() => {
    if (!wsStatus.connected) return;

    const refreshInterval = setInterval(async () => {
      try {
        const response = await apiService.getCryptoPrices();
        if (response.success && response.data) {
          const convertedData = response.data.map(convertApiDataToAsset);
          
          setCryptoData(prev => {
            // Track price changes
            const newPriceChanges: Record<string, 'up' | 'down' | null> = {};
            const newUpdatedIds = new Set<string>();
            
            convertedData.forEach(newCrypto => {
              const oldCrypto = prev.find(c => c.id === newCrypto.id);
              if (oldCrypto && oldCrypto.priceUsd !== newCrypto.priceUsd) {
                newPriceChanges[newCrypto.id] = newCrypto.priceUsd > oldCrypto.priceUsd ? 'up' : 'down';
                newUpdatedIds.add(newCrypto.id);
              }
            });
            
            setPriceChanges(newPriceChanges);
            setUpdatedIds(newUpdatedIds);
            
            // Clear animations after 1 second
            setTimeout(() => {
              setUpdatedIds(new Set());
              setPriceChanges({});
            }, 1000);
            
            return convertedData;
          });
          
          setWsStatus(prev => ({ ...prev, lastUpdate: new Date() }));
        }
      } catch (err) {
        console.error('Error refreshing crypto data:', err);
      }
    }, 30000); // 30 seconds

    return () => clearInterval(refreshInterval);
  }, [wsStatus.connected]);

  const sortData = useCallback((field: SortField, direction: SortDirection) => {
    setCryptoData(prev => {
      const sorted = [...prev].sort((a, b) => {
        const aVal = a[field];
        const bVal = b[field];

        if (typeof aVal === 'string' && typeof bVal === 'string') {
          return direction === 'asc' 
            ? aVal.toLowerCase().localeCompare(bVal.toLowerCase())
            : bVal.toLowerCase().localeCompare(aVal.toLowerCase());
        }

        if (typeof aVal === 'number' && typeof bVal === 'number') {
          return direction === 'asc' ? aVal - bVal : bVal - aVal;
        }

        return 0;
      });
      return sorted;
    });
  }, []);

  const filterData = useCallback((searchTerm: string) => {
    // Note: Filtering is now handled by the parent component
    // This function is kept for compatibility
  }, []);

  const refreshData = useCallback(async () => {
    await fetchCryptoData();
  }, [fetchCryptoData]);

  return {
    cryptoData,
    isLoading,
    error,
    wsStatus,
    updatedIds,
    priceChanges,
    sortData,
    filterData,
    refreshData,
  };
};
