import { useState } from 'react';
import { CryptoAsset } from '@/types/crypto';
import { useCryptoData } from '@/hooks/useCryptoData';
import Header from '@/components/Header';
import QuickStats from '@/components/QuickStats';
import CryptoTable from '@/components/CryptoTable';
import PriceChart from '@/components/PriceChart';
import Footer from '@/components/Footer';

const Index = () => {
  const { cryptoData, wsStatus, updatedIds, priceChanges, sortData, filterData } = useCryptoData();
  const [selectedCoin, setSelectedCoin] = useState<CryptoAsset | null>(null);

  return (
    <div className="min-h-screen flex flex-col">
      <Header wsStatus={wsStatus} />
      
      <main className="flex-1 container px-4 py-6 space-y-6">
        {/* Quick Stats */}
        <section className="animate-fade-in">
          <QuickStats />
        </section>

        {/* Main Content */}
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          {/* Crypto Table */}
          <section className="xl:col-span-2 animate-slide-up" style={{ animationDelay: '0.1s' }}>
            <CryptoTable
              data={cryptoData}
              updatedIds={updatedIds}
              priceChanges={priceChanges}
              onSort={sortData}
              onFilter={filterData}
              onSelectCoin={setSelectedCoin}
              selectedCoinId={selectedCoin?.id}
            />
          </section>

          {/* Price Chart */}
          <section className="animate-slide-up" style={{ animationDelay: '0.2s' }}>
            <PriceChart selectedCoin={selectedCoin} />
          </section>
        </div>
      </main>

      <Footer wsStatus={wsStatus} />
    </div>
  );
};

export default Index;
