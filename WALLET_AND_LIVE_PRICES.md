# Wallet Connection & Live Prices Guide 🚀

## ✅ What's Been Added:

### 1. **Wallet Connection Feature**
- Connect MetaMask or any Web3 wallet
- View wallet address and ETH balance
- Automatic reconnection on page reload
- Disconnect wallet option

### 2. **Live Cryptocurrency Prices**
- Real-time data from CoinGecko API
- Prices update automatically
- Shows actual market data (not mock data)

---

## 🔌 How to Connect Your Wallet:

### Step 1: Install MetaMask
If you don't have MetaMask:
1. Visit: https://metamask.io/download/
2. Install the browser extension
3. Create a new wallet or import existing one

### Step 2: Connect Wallet to Website
1. Start the application (see below)
2. Login to your account
3. Click **"Connect Wallet"** button in the header
4. MetaMask popup will appear
5. Click **"Connect"** in MetaMask
6. Your wallet is now connected! ✅

### What You'll See:
- Your wallet address (shortened): `0x1234...5678`
- Your ETH balance: `0.1234 ETH`
- Click on wallet to see full address or disconnect

---

## 💰 Live Cryptocurrency Prices:

### How It Works:
1. **CoinGecko API Integration**: 
   - Backend fetches real-time data from CoinGecko
   - Updates every 30 seconds automatically
   - Shows top 50 cryptocurrencies by market cap

2. **What Data is Live**:
   - ✅ Current Price (USD)
   - ✅ 24h Price Change %
   - ✅ Market Cap
   - ✅ Trading Volume
   - ✅ 7-day Price Chart
   - ✅ All-Time High/Low

3. **API Status**:
   - Your CoinGecko API Key: `CG-sYDm3JAhzp23TaP6CUypcJjX`
   - Status: **ACTIVE** ✅
   - Rate Limit: Free tier (30 calls/minute)

### To Verify Prices are Live:
1. Open the dashboard
2. Note the price of Bitcoin
3. Open https://www.coingecko.com/en/coins/bitcoin
4. Compare prices - they should match! 📊

---

## 🚀 How to Start the Application:

### Quick Start (Recommended):
```bash
cd "d:\AWS Project"
START-APP-TEST-MODE.bat
```

### Manual Start:

**Terminal 1 - Backend:**
```bash
cd "d:\AWS Project\crypto-tracker-backend\crypto-tracker-backend"
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd "d:\AWS Project\crypto-pulse-dashboard"
npm run dev
```

### Access:
- Open: http://localhost:8080
- Login with your account
- Click "Connect Wallet" in header

---

## 🔍 Features Overview:

### Wallet Features:
- ✅ Connect MetaMask wallet
- ✅ View wallet address
- ✅ View ETH balance
- ✅ Auto-reconnect on refresh
- ✅ Disconnect wallet
- ✅ Network detection (Ethereum Mainnet, etc.)

### Price Features:
- ✅ Live prices from CoinGecko
- ✅ Real-time updates (30s interval)
- ✅ Price change indicators (green/red)
- ✅ 7-day sparkline charts
- ✅ Market cap rankings
- ✅ Volume data

---

## 📊 API Configuration:

### Current Setup:
```
CoinGecko API URL: https://api.coingecko.com/api/v3/coins/markets
API Key: CG-sYDm3JAhzp23TaP6CUypcJjX
Currency: USD
Per Page: 50 coins
Update Interval: 30 seconds
```

### API Response Example:
```json
{
  "success": true,
  "message": "Live crypto prices fetched successfully from CoinGecko",
  "data": [
    {
      "id": "bitcoin",
      "symbol": "btc",
      "name": "Bitcoin",
      "current_price": 45234.56,
      "price_change_percentage_24h": 2.34,
      "market_cap": 885000000000,
      ...
    }
  ],
  "count": 50
}
```

---

## 🧪 Testing:

### Test Wallet Connection:
1. Click "Connect Wallet"
2. Approve in MetaMask
3. See your address in header
4. Click wallet dropdown to see balance
5. Click "Disconnect Wallet" to test disconnect

### Test Live Prices:
1. Open dashboard
2. Note Bitcoin price
3. Wait 30 seconds
4. Price should update automatically
5. Compare with CoinGecko.com to verify

### Test Price Updates:
1. Open browser console (F12)
2. Look for: "Fetching LIVE crypto prices from CoinGecko"
3. Should appear every 30 seconds
4. Status should show "Live" (green dot)

---

## 🔧 Troubleshooting:

### Wallet Won't Connect:
- Make sure MetaMask is installed
- Check if MetaMask is unlocked
- Try refreshing the page
- Check browser console for errors

### Prices Not Updating:
- Check backend is running
- Look for "Live" status in header
- Check browser console for API errors
- Verify internet connection

### API Rate Limit:
- Free tier: 30 calls/minute
- If exceeded, wait 1 minute
- Backend will show fallback mock data

---

## 📝 Important Notes:

### About Live Prices:
- ✅ **YES, prices are LIVE from CoinGecko**
- ✅ Updates every 30 seconds
- ✅ Real market data, not fake
- ✅ Same data as CoinGecko website

### About Wallet:
- ✅ Supports MetaMask and Web3 wallets
- ✅ Secure connection (no private keys stored)
- ✅ Works with Ethereum mainnet and testnets
- ✅ Shows real ETH balance

### Security:
- 🔒 Private keys never leave your wallet
- 🔒 Website only reads public address
- 🔒 Cannot make transactions without approval
- 🔒 Safe to connect

---

## 🎯 Next Steps:

1. **Start the application**
2. **Login to your account**
3. **Connect your MetaMask wallet**
4. **View live cryptocurrency prices**
5. **Watch prices update in real-time**

---

## 💡 Pro Tips:

- Keep MetaMask unlocked for auto-reconnect
- Refresh page if wallet doesn't connect
- Check "Live" status to confirm API is working
- Compare prices with CoinGecko.com to verify accuracy
- Use Ethereum Mainnet for accurate balance display

---

**Enjoy tracking live crypto prices with your connected wallet! 🚀📈**
