import { useState, useEffect, useMemo } from 'react';
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import { CryptoAsset, TimeInterval, PriceHistory } from '@/types/crypto';
import { generatePriceHistory } from '@/data/mockCryptoData';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';

interface PriceChartProps {
  selectedCoin: CryptoAsset | null;
}

const timeIntervals: TimeInterval[] = ['1H', '24H', '7D', '30D'];

const formatPrice = (value: number): string => {
  if (value >= 1000) return `$${value.toLocaleString(undefined, { maximumFractionDigits: 0 })}`;
  if (value >= 1) return `$${value.toFixed(2)}`;
  return `$${value.toFixed(4)}`;
};

const formatTime = (timestamp: number, interval: TimeInterval): string => {
  const date = new Date(timestamp);
  switch (interval) {
    case '1H':
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    case '24H':
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    case '7D':
      return date.toLocaleDateString([], { weekday: 'short', day: 'numeric' });
    case '30D':
      return date.toLocaleDateString([], { month: 'short', day: 'numeric' });
  }
};

const CustomTooltip = ({ active, payload, label, interval }: any) => {
  if (active && payload && payload.length) {
    return (
      <div className="glass-card p-3 border border-border/50">
        <p className="text-xs text-muted-foreground mb-1">
          {formatTime(label, interval)}
        </p>
        <p className="text-sm font-bold font-mono">
          {formatPrice(payload[0].value)}
        </p>
      </div>
    );
  }
  return null;
};

const PriceChart = ({ selectedCoin }: PriceChartProps) => {
  const [interval, setInterval] = useState<TimeInterval>('24H');
  const [priceHistory, setPriceHistory] = useState<PriceHistory[]>([]);

  useEffect(() => {
    if (selectedCoin) {
      const history = generatePriceHistory(selectedCoin.priceUsd, interval);
      setPriceHistory(history);
    }
  }, [selectedCoin, interval]);

  const chartData = useMemo(() => {
    return priceHistory.map((point) => ({
      time: point.time,
      price: point.priceUsd,
    }));
  }, [priceHistory]);

  const priceChange = useMemo(() => {
    if (chartData.length < 2) return 0;
    const first = chartData[0].price;
    const last = chartData[chartData.length - 1].price;
    return ((last - first) / first) * 100;
  }, [chartData]);

  const isPositive = priceChange >= 0;
  const gradientColor = isPositive ? 'hsl(150, 100%, 45%)' : 'hsl(0, 72%, 51%)';

  if (!selectedCoin) {
    return (
      <div className="glass-card p-6 h-[400px] flex items-center justify-center">
        <div className="text-center text-muted-foreground">
          <p className="text-lg mb-2">Select a cryptocurrency</p>
          <p className="text-sm">Click on any row to view detailed price chart</p>
        </div>
      </div>
    );
  }

  return (
    <div className="glass-card p-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
        <div>
          <h3 className="text-xl font-bold">{selectedCoin.name} Price</h3>
          <div className="flex items-center gap-2 mt-1">
            <span className="text-2xl font-bold font-mono">
              {formatPrice(selectedCoin.priceUsd)}
            </span>
            <span
              className={cn(
                'text-sm font-medium px-2 py-0.5 rounded',
                isPositive ? 'bg-success/20 text-success' : 'bg-destructive/20 text-destructive'
              )}
            >
              {isPositive ? '+' : ''}{priceChange.toFixed(2)}%
            </span>
          </div>
        </div>

        {/* Time Interval Selector */}
        <div className="flex gap-1 bg-secondary/50 p-1 rounded-lg">
          {timeIntervals.map((int) => (
            <Button
              key={int}
              variant={interval === int ? 'default' : 'ghost'}
              size="sm"
              onClick={() => setInterval(int)}
              className={cn(
                'px-4 font-medium',
                interval === int && 'btn-glow'
              )}
            >
              {int}
            </Button>
          ))}
        </div>
      </div>

      {/* Chart */}
      <div className="h-[280px]">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={chartData} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
            <defs>
              <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor={gradientColor} stopOpacity={0.3} />
                <stop offset="95%" stopColor={gradientColor} stopOpacity={0} />
              </linearGradient>
            </defs>
            <XAxis
              dataKey="time"
              tickFormatter={(value) => formatTime(value, interval)}
              stroke="hsl(var(--muted-foreground))"
              fontSize={11}
              tickLine={false}
              axisLine={false}
              minTickGap={50}
            />
            <YAxis
              domain={['dataMin', 'dataMax']}
              tickFormatter={formatPrice}
              stroke="hsl(var(--muted-foreground))"
              fontSize={11}
              tickLine={false}
              axisLine={false}
              width={80}
            />
            <Tooltip content={<CustomTooltip interval={interval} />} />
            <Area
              type="monotone"
              dataKey="price"
              stroke={gradientColor}
              strokeWidth={2}
              fillOpacity={1}
              fill="url(#colorPrice)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default PriceChart;
