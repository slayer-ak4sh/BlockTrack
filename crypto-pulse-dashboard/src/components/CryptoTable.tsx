import { useState } from 'react';
import { ArrowUpDown, ArrowUp, ArrowDown, Search, TrendingUp, TrendingDown } from 'lucide-react';
import { CryptoAsset, SortField, SortDirection } from '@/types/crypto';
import { Input } from '@/components/ui/input';
import Sparkline from './Sparkline';
import { cn } from '@/lib/utils';

interface CryptoTableProps {
  data: CryptoAsset[];
  updatedIds: Set<string>;
  priceChanges: Record<string, 'up' | 'down' | null>;
  onSort: (field: SortField, direction: SortDirection) => void;
  onFilter: (searchTerm: string) => void;
  onSelectCoin: (coin: CryptoAsset) => void;
  selectedCoinId?: string;
}

const formatCurrency = (value: number): string => {
  if (value >= 1e12) return `$${(value / 1e12).toFixed(2)}T`;
  if (value >= 1e9) return `$${(value / 1e9).toFixed(2)}B`;
  if (value >= 1e6) return `$${(value / 1e6).toFixed(2)}M`;
  return `$${value.toLocaleString()}`;
};

const formatPrice = (value: number): string => {
  if (value >= 1000) return `$${value.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  if (value >= 1) return `$${value.toFixed(2)}`;
  if (value >= 0.01) return `$${value.toFixed(4)}`;
  return `$${value.toFixed(6)}`;
};

const coinIcons: Record<string, { bg: string; symbol: string }> = {
  BTC: { bg: '#F7931A', symbol: '₿' },
  ETH: { bg: '#627EEA', symbol: 'Ξ' },
  USDT: { bg: '#26A17B', symbol: '₮' },
  BNB: { bg: '#F3BA2F', symbol: 'B' },
  SOL: { bg: '#9945FF', symbol: 'S' },
  XRP: { bg: '#23292F', symbol: 'X' },
  ADA: { bg: '#0033AD', symbol: 'A' },
  DOGE: { bg: '#C2A633', symbol: 'Ð' },
  AVAX: { bg: '#E84142', symbol: 'A' },
  DOT: { bg: '#E6007A', symbol: '●' },
  LINK: { bg: '#2A5ADA', symbol: '⬡' },
  MATIC: { bg: '#8247E5', symbol: 'M' },
};

const CryptoTable = ({
  data,
  updatedIds,
  priceChanges,
  onSort,
  onFilter,
  onSelectCoin,
  selectedCoinId,
}: CryptoTableProps) => {
  const [sortField, setSortField] = useState<SortField>('rank');
  const [sortDirection, setSortDirection] = useState<SortDirection>('asc');
  const [searchTerm, setSearchTerm] = useState('');

  const handleSort = (field: SortField) => {
    const newDirection = field === sortField && sortDirection === 'asc' ? 'desc' : 'asc';
    setSortField(field);
    setSortDirection(newDirection);
    onSort(field, newDirection);
  };

  const handleSearch = (value: string) => {
    setSearchTerm(value);
    onFilter(value);
  };

  const SortIcon = ({ field }: { field: SortField }) => {
    if (sortField !== field) return <ArrowUpDown className="h-3 w-3 opacity-50" />;
    return sortDirection === 'asc' ? (
      <ArrowUp className="h-3 w-3 text-primary" />
    ) : (
      <ArrowDown className="h-3 w-3 text-primary" />
    );
  };

  return (
    <div className="glass-card overflow-hidden">
      {/* Search Bar */}
      <div className="p-4 border-b border-border/50">
        <div className="relative max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            type="text"
            placeholder="Search by name or symbol..."
            value={searchTerm}
            onChange={(e) => handleSearch(e.target.value)}
            className="pl-10 bg-secondary/50 border-border/50 input-glow"
          />
        </div>
      </div>

      {/* Table */}
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="sticky top-0 bg-card/95 backdrop-blur-sm border-b border-border/50">
            <tr>
              <th
                className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:text-foreground transition-colors"
                onClick={() => handleSort('rank')}
              >
                <div className="flex items-center gap-1">
                  # <SortIcon field="rank" />
                </div>
              </th>
              <th
                className="px-4 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:text-foreground transition-colors"
                onClick={() => handleSort('name')}
              >
                <div className="flex items-center gap-1">
                  Name <SortIcon field="name" />
                </div>
              </th>
              <th
                className="px-4 py-3 text-right text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:text-foreground transition-colors"
                onClick={() => handleSort('priceUsd')}
              >
                <div className="flex items-center justify-end gap-1">
                  Price <SortIcon field="priceUsd" />
                </div>
              </th>
              <th
                className="px-4 py-3 text-right text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:text-foreground transition-colors"
                onClick={() => handleSort('changePercent24Hr')}
              >
                <div className="flex items-center justify-end gap-1">
                  24h % <SortIcon field="changePercent24Hr" />
                </div>
              </th>
              <th
                className="px-4 py-3 text-right text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:text-foreground transition-colors hidden md:table-cell"
                onClick={() => handleSort('volumeUsd24Hr')}
              >
                <div className="flex items-center justify-end gap-1">
                  Volume (24h) <SortIcon field="volumeUsd24Hr" />
                </div>
              </th>
              <th
                className="px-4 py-3 text-right text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:text-foreground transition-colors hidden lg:table-cell"
                onClick={() => handleSort('marketCapUsd')}
              >
                <div className="flex items-center justify-end gap-1">
                  Market Cap <SortIcon field="marketCapUsd" />
                </div>
              </th>
              <th className="px-4 py-3 text-right text-xs font-medium text-muted-foreground uppercase tracking-wider hidden xl:table-cell">
                Last 24h
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-border/30">
            {data.map((crypto) => {
              const isUpdated = updatedIds.has(crypto.id);
              const priceChange = priceChanges[crypto.id];
              const icon = coinIcons[crypto.symbol] || { bg: '#666', symbol: crypto.symbol[0] };
              
              return (
                <tr
                  key={crypto.id}
                  onClick={() => onSelectCoin(crypto)}
                  className={cn(
                    'crypto-row cursor-pointer',
                    isUpdated && priceChange === 'up' && 'row-update-up',
                    isUpdated && priceChange === 'down' && 'row-update-down',
                    selectedCoinId === crypto.id && 'bg-secondary/50'
                  )}
                >
                  <td className="px-4 py-4 whitespace-nowrap">
                    <span className="text-sm text-muted-foreground font-mono">{crypto.rank}</span>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    <div className="flex items-center gap-3">
                      <div
                        className="h-8 w-8 rounded-full flex items-center justify-center text-sm font-bold"
                        style={{ backgroundColor: `${icon.bg}20`, color: icon.bg }}
                      >
                        {icon.symbol}
                      </div>
                      <div>
                        <div className="font-semibold">{crypto.name}</div>
                        <div className="text-xs text-muted-foreground">{crypto.symbol}</div>
                      </div>
                    </div>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap text-right">
                    <span className="font-mono font-medium">{formatPrice(crypto.priceUsd)}</span>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap text-right">
                    <div
                      className={cn(
                        'inline-flex items-center gap-1 font-medium',
                        crypto.changePercent24Hr >= 0 ? 'price-up' : 'price-down'
                      )}
                    >
                      {crypto.changePercent24Hr >= 0 ? (
                        <TrendingUp className="h-3 w-3" />
                      ) : (
                        <TrendingDown className="h-3 w-3" />
                      )}
                      <span className="font-mono">
                        {crypto.changePercent24Hr >= 0 ? '+' : ''}
                        {crypto.changePercent24Hr.toFixed(2)}%
                      </span>
                    </div>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap text-right hidden md:table-cell">
                    <span className="font-mono text-sm text-muted-foreground">
                      {formatCurrency(crypto.volumeUsd24Hr)}
                    </span>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap text-right hidden lg:table-cell">
                    <span className="font-mono text-sm text-muted-foreground">
                      {formatCurrency(crypto.marketCapUsd)}
                    </span>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap text-right hidden xl:table-cell">
                    <div className="flex justify-end">
                      <Sparkline
                        data={crypto.sparkline}
                        positive={crypto.changePercent24Hr >= 0}
                      />
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default CryptoTable;
