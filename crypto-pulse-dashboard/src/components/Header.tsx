import { Activity, Bell, Settings, User, LogOut, Wallet } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import { WebSocketStatus } from '@/types/crypto';
import { Button } from '@/components/ui/button';
import { useAuth } from '@/contexts/AuthContext';
import { useWallet } from '@/contexts/WalletContext';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

interface HeaderProps {
  wsStatus: WebSocketStatus;
}

const Header = ({ wsStatus }: HeaderProps) => {
  const { user, isAuthenticated, logout } = useAuth();
  const { address, isConnected, isConnecting, balance, connectWallet, disconnectWallet } = useWallet();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };
  
  const getStatusClass = () => {
    if (wsStatus.connecting) return 'status-connecting';
    if (wsStatus.connected) return 'status-connected';
    return 'status-disconnected';
  };

  const getStatusText = () => {
    if (wsStatus.connecting) return 'Connecting...';
    if (wsStatus.connected) return 'Live';
    return 'Disconnected';
  };

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border/50 bg-background/80 backdrop-blur-xl">
      <div className="container flex h-16 items-center justify-between px-4">
        {/* Logo */}
        <Link to="/" className="flex items-center gap-3">
          <div className="relative">
            <div className="absolute inset-0 rounded-lg bg-primary/20 blur-lg" />
            <div className="relative flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-br from-primary to-primary/60">
              <Activity className="h-5 w-5 text-primary-foreground" />
            </div>
          </div>
          <div className="flex flex-col">
            <span className="text-lg font-bold gradient-text">Crypto Tracker</span>
            <span className="text-[10px] font-medium uppercase tracking-wider text-muted-foreground">Pro</span>
          </div>
        </Link>

        {/* Connection Status */}
        <div className="hidden md:flex items-center gap-6">
          <div className="flex items-center gap-2 rounded-full bg-secondary/50 px-3 py-1.5">
            <div className={`status-dot ${getStatusClass()}`} />
            <span className="text-sm font-medium">{getStatusText()}</span>
            {wsStatus.connected && (
              <span className="text-xs text-muted-foreground">WebSocket</span>
            )}
          </div>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-2">
          {isAuthenticated ? (
            <>
              {/* Wallet Connection */}
              {isConnected ? (
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="outline" className="gap-2">
                      <Wallet className="h-4 w-4" />
                      <span className="hidden sm:inline">
                        {address?.slice(0, 6)}...{address?.slice(-4)}
                      </span>
                      {balance && (
                        <span className="text-xs text-muted-foreground hidden md:inline">
                          {balance} ETH
                        </span>
                      )}
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end" className="w-56">
                    <div className="px-2 py-1.5">
                      <p className="text-sm font-medium">Wallet Address</p>
                      <p className="text-xs text-muted-foreground break-all">{address}</p>
                    </div>
                    {balance && (
                      <div className="px-2 py-1">
                        <p className="text-sm font-medium">Balance</p>
                        <p className="text-xs text-muted-foreground">{balance} ETH</p>
                      </div>
                    )}
                    <DropdownMenuSeparator />
                    <DropdownMenuItem onClick={disconnectWallet} className="cursor-pointer text-red-600">
                      Disconnect Wallet
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              ) : (
                <Button 
                  onClick={connectWallet} 
                  disabled={isConnecting}
                  className="gap-2"
                >
                  <Wallet className="h-4 w-4" />
                  <span className="hidden sm:inline">
                    {isConnecting ? 'Connecting...' : 'Connect Wallet'}
                  </span>
                </Button>
              )}

              <Button variant="ghost" size="icon" className="relative">
                <Bell className="h-5 w-5" />
                <span className="absolute -top-0.5 -right-0.5 h-2 w-2 rounded-full bg-primary" />
              </Button>

              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" size="icon" className="rounded-full">
                    <div className="h-8 w-8 rounded-full bg-gradient-to-br from-primary/80 to-primary/40 flex items-center justify-center">
                      <User className="h-4 w-4" />
                    </div>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-48">
                  <div className="px-2 py-1.5 text-sm font-medium">
                    {user?.username}
                  </div>
                  <div className="px-2 py-1 text-xs text-muted-foreground">
                    {user?.email}
                  </div>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem asChild>
                    <Link to="/profile" className="flex items-center gap-2 cursor-pointer">
                      <User className="h-4 w-4" />
                      Profile
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link to="/settings" className="flex items-center gap-2 cursor-pointer">
                      <Settings className="h-4 w-4" />
                      Settings
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem onClick={handleLogout} className="cursor-pointer text-red-600">
                    <LogOut className="h-4 w-4 mr-2" />
                    Sign Out
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </>
          ) : (
            <div className="flex items-center gap-2">
              <Button variant="ghost" asChild>
                <Link to="/login">Sign In</Link>
              </Button>
              <Button asChild>
                <Link to="/signup">Sign Up</Link>
              </Button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;