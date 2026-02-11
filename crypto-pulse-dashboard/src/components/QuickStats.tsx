import { TrendingUp, TrendingDown, DollarSign, BarChart3 } from 'lucide-react';
import { getTopStats } from '@/data/mockCryptoData';

const formatCurrency = (value: number): string => {
  if (value >= 1e12) return `$${(value / 1e12).toFixed(2)}T`;
  if (value >= 1e9) return `$${(value / 1e9).toFixed(2)}B`;
  if (value >= 1e6) return `$${(value / 1e6).toFixed(2)}M`;
  return `$${value.toLocaleString()}`;
};

const formatPrice = (value: number): string => {
  if (value >= 1000) return `$${value.toLocaleString(undefined, { maximumFractionDigits: 2 })}`;
  if (value >= 1) return `$${value.toFixed(2)}`;
  return `$${value.toFixed(4)}`;
};

const QuickStats = () => {
  const stats = getTopStats();

  return (
    <div className="w-full overflow-x-auto scrollbar-thin pb-2">
      <div className="flex gap-4 min-w-max px-4">
        {/* Bitcoin Card */}
        <div className="stat-card min-w-[200px] flex-1">
          <div className="flex items-center justify-between mb-2">
            <div className="flex items-center gap-2">
              <div className="h-8 w-8 rounded-full bg-[#F7931A]/20 flex items-center justify-center">
                <span className="text-sm font-bold text-[#F7931A]">₿</span>
              </div>
              <span className="font-semibold">Bitcoin</span>
            </div>
            <span className="text-xs text-muted-foreground">BTC</span>
          </div>
          <div className="flex items-end justify-between">
            <span className="text-xl font-bold font-mono">{formatPrice(stats.btc.price)}</span>
            <div className={`flex items-center gap-1 ${stats.btc.change >= 0 ? 'price-up' : 'price-down'}`}>
              {stats.btc.change >= 0 ? (
                <TrendingUp className="h-4 w-4" />
              ) : (
                <TrendingDown className="h-4 w-4" />
              )}
              <span className="text-sm font-medium">{stats.btc.change.toFixed(2)}%</span>
            </div>
          </div>
          <div className="mt-2 text-xs text-muted-foreground">
            Dominance: <span className="text-foreground">{stats.btc.dominance}%</span>
          </div>
        </div>

        {/* Ethereum Card */}
        <div className="stat-card min-w-[200px] flex-1">
          <div className="flex items-center justify-between mb-2">
            <div className="flex items-center gap-2">
              <div className="h-8 w-8 rounded-full bg-[#627EEA]/20 flex items-center justify-center">
                <span className="text-sm font-bold text-[#627EEA]">Ξ</span>
              </div>
              <span className="font-semibold">Ethereum</span>
            </div>
            <span className="text-xs text-muted-foreground">ETH</span>
          </div>
          <div className="flex items-end justify-between">
            <span className="text-xl font-bold font-mono">{formatPrice(stats.eth.price)}</span>
            <div className={`flex items-center gap-1 ${stats.eth.change >= 0 ? 'price-up' : 'price-down'}`}>
              {stats.eth.change >= 0 ? (
                <TrendingUp className="h-4 w-4" />
              ) : (
                <TrendingDown className="h-4 w-4" />
              )}
              <span className="text-sm font-medium">{stats.eth.change.toFixed(2)}%</span>
            </div>
          </div>
          <div className="mt-2 text-xs text-muted-foreground">
            Dominance: <span className="text-foreground">{stats.eth.dominance}%</span>
          </div>
        </div>

        {/* Market Cap Card */}
        <div className="stat-card min-w-[180px] flex-1">
          <div className="flex items-center gap-2 mb-2">
            <div className="h-8 w-8 rounded-full bg-primary/20 flex items-center justify-center">
              <DollarSign className="h-4 w-4 text-primary" />
            </div>
            <span className="font-semibold">Market Cap</span>
          </div>
          <span className="text-xl font-bold font-mono">{formatCurrency(stats.totalMarketCap)}</span>
          <div className="mt-2 text-xs text-muted-foreground">
            Total crypto market
          </div>
        </div>

        {/* 24h Volume Card */}
        <div className="stat-card min-w-[180px] flex-1">
          <div className="flex items-center gap-2 mb-2">
            <div className="h-8 w-8 rounded-full bg-success/20 flex items-center justify-center">
              <BarChart3 className="h-4 w-4 text-success" />
            </div>
            <span className="font-semibold">24h Volume</span>
          </div>
          <span className="text-xl font-bold font-mono">{formatCurrency(stats.total24hVolume)}</span>
          <div className="mt-2 text-xs text-muted-foreground">
            Trading volume
          </div>
        </div>
      </div>
    </div>
  );
};

export default QuickStats;
