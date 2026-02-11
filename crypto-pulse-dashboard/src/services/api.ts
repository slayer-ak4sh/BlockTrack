const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';
const USE_TEST_MODE = true; // Set to false when DynamoDB is configured

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
  count?: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface User {
  username: string;
  email: string;
  role?: string;
}

export interface CryptoPrice {
  id: string;
  symbol: string;
  name: string;
  image: string;
  current_price: number;
  market_cap: number;
  market_cap_rank: number;
  fully_diluted_valuation?: number;
  total_volume: number;
  high_24h: number;
  low_24h: number;
  price_change_24h: number;
  price_change_percentage_24h: number;
  market_cap_change_24h: number;
  market_cap_change_percentage_24h: number;
  circulating_supply: number;
  total_supply?: number;
  max_supply?: number;
  ath: number;
  ath_change_percentage: number;
  ath_date: string;
  atl: number;
  atl_change_percentage: number;
  atl_date: string;
  last_updated: string;
  sparkline_in_7d?: {
    price: number[];
  };
}

class ApiService {
  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<ApiResponse<T>> {
    const url = `${API_BASE_URL}${endpoint}`;
    
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // Auth endpoints
  async login(credentials: LoginRequest): Promise<ApiResponse<{ user: User }>> {
    const endpoint = USE_TEST_MODE ? '/test/auth/login' : '/auth/login';
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(credentials),
    });
  }

  async register(userData: RegisterRequest): Promise<ApiResponse<{ user: User }>> {
    const endpoint = USE_TEST_MODE ? '/test/auth/register' : '/auth/register';
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(userData),
    });
  }

  async checkUsername(username: string): Promise<ApiResponse<{ exists: boolean }>> {
    return this.request(`/auth/check/${username}`);
  }

  // Crypto endpoints
  async getCryptoPrices(): Promise<ApiResponse<CryptoPrice[]>> {
    const endpoint = USE_TEST_MODE ? '/test/crypto/prices' : '/crypto/prices';
    return this.request(endpoint);
  }

  async getStoredCryptoPrices(): Promise<ApiResponse<CryptoPrice[]>> {
    return this.request('/crypto/prices/stored');
  }

  async getCryptoBySymbol(symbol: string): Promise<ApiResponse<CryptoPrice>> {
    return this.request(`/crypto/price/${symbol}`);
  }

  async refreshPrices(): Promise<ApiResponse<{ count: number }>> {
    return this.request('/crypto/refresh', {
      method: 'POST',
    });
  }

  // Health check
  async healthCheck(): Promise<ApiResponse<any>> {
    return this.request('/health');
  }
}

export const apiService = new ApiService();