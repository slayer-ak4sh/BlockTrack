export interface CryptoAsset {
  id: string;
  rank: number;
  symbol: string;
  name: string;
  supply: number;
  maxSupply: number | null;
  marketCapUsd: number;
  volumeUsd24Hr: number;
  priceUsd: number;
  changePercent24Hr: number;
  vwap24Hr: number;
  explorer: string;
  sparkline: number[];
}

export interface PriceHistory {
  priceUsd: number;
  time: number;
  date: string;
}

export interface WebSocketStatus {
  connected: boolean;
  connecting: boolean;
  lastUpdate: Date | null;
}

export interface User {
  id: string;
  email: string;
  name: string;
  avatar?: string;
  createdAt: Date;
}

export type SortField = 'rank' | 'name' | 'priceUsd' | 'changePercent24Hr' | 'volumeUsd24Hr' | 'marketCapUsd';
export type SortDirection = 'asc' | 'desc';
export type TimeInterval = '1H' | '24H' | '7D' | '30D';
