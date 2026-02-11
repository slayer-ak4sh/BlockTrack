import { RefreshCw } from 'lucide-react';
import { WebSocketStatus } from '@/types/crypto';

interface FooterProps {
  wsStatus: WebSocketStatus;
}

const Footer = ({ wsStatus }: FooterProps) => {
  const formatLastUpdate = () => {
    if (!wsStatus.lastUpdate) return 'Never';
    
    const now = new Date();
    const diff = Math.floor((now.getTime() - wsStatus.lastUpdate.getTime()) / 1000);
    
    if (diff < 5) return 'Just now';
    if (diff < 60) return `${diff} seconds ago`;
    if (diff < 3600) return `${Math.floor(diff / 60)} minutes ago`;
    return wsStatus.lastUpdate.toLocaleTimeString();
  };

  return (
    <footer className="border-t border-border/50 bg-card/50 backdrop-blur-sm">
      <div className="container px-4 py-3 flex flex-col sm:flex-row items-center justify-between gap-2">
        <div className="flex items-center gap-4 text-sm text-muted-foreground">
          <span>© 2025 Crypto Tracker Pro</span>
          <span className="hidden sm:inline">•</span>
          <span className="hidden sm:inline">Real-time cryptocurrency data</span>
        </div>
        
        <div className="flex items-center gap-2 text-sm">
          {wsStatus.connected && (
            <RefreshCw className="h-3 w-3 text-primary animate-spin" style={{ animationDuration: '3s' }} />
          )}
          <span className="text-muted-foreground">
            Last update: <span className="text-foreground">{formatLastUpdate()}</span>
          </span>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
