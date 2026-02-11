import { CryptoAsset, PriceHistory } from '@/types/crypto';

// Generate realistic sparkline data
const generateSparkline = (basePrice: number, volatility: number = 0.05): number[] => {
  const points = 24;
  const data: number[] = [];
  let price = basePrice * (1 - volatility);
  
  for (let i = 0; i < points; i++) {
    const change = (Math.random() - 0.5) * volatility * basePrice;
    price = Math.max(price + change, basePrice * 0.8);
    price = Math.min(price, basePrice * 1.2);
    data.push(price);
  }
  
  // End near current price
  data[data.length - 1] = basePrice;
  return data;
};

export const mockCryptoData: CryptoAsset[] = [
  {
    id: 'bitcoin',
    rank: 1,
    symbol: 'BTC',
    name: 'Bitcoin',
    supply: 19500000,
    maxSupply: 21000000,
    marketCapUsd: 1890000000000,
    volumeUsd24Hr: 28500000000,
    priceUsd: 96847.23,
    changePercent24Hr: 2.34,
    vwap24Hr: 95234.56,
    explorer: 'https://blockchain.info/',
    sparkline: generateSparkline(96847.23, 0.03),
  },
  {
    id: 'ethereum',
    rank: 2,
    symbol: 'ETH',
    name: 'Ethereum',
    supply: 120000000,
    maxSupply: null,
    marketCapUsd: 420000000000,
    volumeUsd24Hr: 15800000000,
    priceUsd: 3487.92,
    changePercent24Hr: -1.23,
    vwap24Hr: 3512.45,
    explorer: 'https://etherscan.io/',
    sparkline: generateSparkline(3487.92, 0.04),
  },
  {
    id: 'tether',
    rank: 3,
    symbol: 'USDT',
    name: 'Tether',
    supply: 92000000000,
    maxSupply: null,
    marketCapUsd: 92000000000,
    volumeUsd24Hr: 45000000000,
    priceUsd: 1.0001,
    changePercent24Hr: 0.01,
    vwap24Hr: 1.0,
    explorer: 'https://www.omniexplorer.info/',
    sparkline: generateSparkline(1.0001, 0.001),
  },
  {
    id: 'binance-coin',
    rank: 4,
    symbol: 'BNB',
    name: 'BNB',
    supply: 153000000,
    maxSupply: 200000000,
    marketCapUsd: 98000000000,
    volumeUsd24Hr: 1200000000,
    priceUsd: 642.18,
    changePercent24Hr: 3.45,
    vwap24Hr: 628.90,
    explorer: 'https://bscscan.com/',
    sparkline: generateSparkline(642.18, 0.05),
  },
  {
    id: 'solana',
    rank: 5,
    symbol: 'SOL',
    name: 'Solana',
    supply: 440000000,
    maxSupply: null,
    marketCapUsd: 82000000000,
    volumeUsd24Hr: 3200000000,
    priceUsd: 186.42,
    changePercent24Hr: 5.67,
    vwap24Hr: 178.23,
    explorer: 'https://explorer.solana.com/',
    sparkline: generateSparkline(186.42, 0.06),
  },
  {
    id: 'xrp',
    rank: 6,
    symbol: 'XRP',
    name: 'XRP',
    supply: 54000000000,
    maxSupply: 100000000000,
    marketCapUsd: 68000000000,
    volumeUsd24Hr: 2800000000,
    priceUsd: 1.26,
    changePercent24Hr: -2.89,
    vwap24Hr: 1.32,
    explorer: 'https://xrpcharts.ripple.com/',
    sparkline: generateSparkline(1.26, 0.07),
  },
  {
    id: 'cardano',
    rank: 7,
    symbol: 'ADA',
    name: 'Cardano',
    supply: 35000000000,
    maxSupply: 45000000000,
    marketCapUsd: 35000000000,
    volumeUsd24Hr: 890000000,
    priceUsd: 0.98,
    changePercent24Hr: 1.12,
    vwap24Hr: 0.96,
    explorer: 'https://cardanoscan.io/',
    sparkline: generateSparkline(0.98, 0.05),
  },
  {
    id: 'dogecoin',
    rank: 8,
    symbol: 'DOGE',
    name: 'Dogecoin',
    supply: 142000000000,
    maxSupply: null,
    marketCapUsd: 28000000000,
    volumeUsd24Hr: 1500000000,
    priceUsd: 0.197,
    changePercent24Hr: 8.45,
    vwap24Hr: 0.185,
    explorer: 'https://dogechain.info/',
    sparkline: generateSparkline(0.197, 0.08),
  },
  {
    id: 'avalanche',
    rank: 9,
    symbol: 'AVAX',
    name: 'Avalanche',
    supply: 395000000,
    maxSupply: 720000000,
    marketCapUsd: 18000000000,
    volumeUsd24Hr: 620000000,
    priceUsd: 45.67,
    changePercent24Hr: -0.78,
    vwap24Hr: 46.12,
    explorer: 'https://snowtrace.io/',
    sparkline: generateSparkline(45.67, 0.06),
  },
  {
    id: 'polkadot',
    rank: 10,
    symbol: 'DOT',
    name: 'Polkadot',
    supply: 1400000000,
    maxSupply: null,
    marketCapUsd: 12000000000,
    volumeUsd24Hr: 380000000,
    priceUsd: 8.56,
    changePercent24Hr: 4.23,
    vwap24Hr: 8.21,
    explorer: 'https://polkascan.io/',
    sparkline: generateSparkline(8.56, 0.05),
  },
  {
    id: 'chainlink',
    rank: 11,
    symbol: 'LINK',
    name: 'Chainlink',
    supply: 608000000,
    maxSupply: 1000000000,
    marketCapUsd: 10500000000,
    volumeUsd24Hr: 520000000,
    priceUsd: 17.28,
    changePercent24Hr: 2.87,
    vwap24Hr: 16.89,
    explorer: 'https://etherscan.io/',
    sparkline: generateSparkline(17.28, 0.05),
  },
  {
    id: 'polygon',
    rank: 12,
    symbol: 'MATIC',
    name: 'Polygon',
    supply: 10000000000,
    maxSupply: 10000000000,
    marketCapUsd: 9200000000,
    volumeUsd24Hr: 410000000,
    priceUsd: 0.92,
    changePercent24Hr: -1.56,
    vwap24Hr: 0.95,
    explorer: 'https://polygonscan.com/',
    sparkline: generateSparkline(0.92, 0.06),
  },
];

export const generatePriceHistory = (basePrice: number, interval: '1H' | '24H' | '7D' | '30D'): PriceHistory[] => {
  const now = Date.now();
  let points: number;
  let intervalMs: number;
  
  switch (interval) {
    case '1H':
      points = 60;
      intervalMs = 60 * 1000; // 1 minute
      break;
    case '24H':
      points = 96;
      intervalMs = 15 * 60 * 1000; // 15 minutes
      break;
    case '7D':
      points = 168;
      intervalMs = 60 * 60 * 1000; // 1 hour
      break;
    case '30D':
      points = 180;
      intervalMs = 4 * 60 * 60 * 1000; // 4 hours
      break;
  }
  
  const history: PriceHistory[] = [];
  let price = basePrice * 0.95;
  const volatility = interval === '1H' ? 0.002 : interval === '24H' ? 0.01 : 0.03;
  
  for (let i = points; i >= 0; i--) {
    const time = now - i * intervalMs;
    const change = (Math.random() - 0.48) * volatility * basePrice;
    price = Math.max(price + change, basePrice * 0.7);
    price = Math.min(price, basePrice * 1.3);
    
    history.push({
      priceUsd: price,
      time,
      date: new Date(time).toISOString(),
    });
  }
  
  // Ensure last point is current price
  history[history.length - 1].priceUsd = basePrice;
  
  return history;
};

export const getTopStats = () => {
  const btc = mockCryptoData.find(c => c.symbol === 'BTC')!;
  const eth = mockCryptoData.find(c => c.symbol === 'ETH')!;
  const totalMarketCap = mockCryptoData.reduce((acc, c) => acc + c.marketCapUsd, 0);
  
  return {
    btc: {
      price: btc.priceUsd,
      change: btc.changePercent24Hr,
      dominance: (btc.marketCapUsd / totalMarketCap * 100).toFixed(1),
    },
    eth: {
      price: eth.priceUsd,
      change: eth.changePercent24Hr,
      dominance: (eth.marketCapUsd / totalMarketCap * 100).toFixed(1),
    },
    totalMarketCap,
    total24hVolume: mockCryptoData.reduce((acc, c) => acc + c.volumeUsd24Hr, 0),
  };
};
