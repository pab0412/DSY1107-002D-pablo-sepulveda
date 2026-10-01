import  { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [status, setStatus] = useState('Comprobando conexión...');
  const [stats, setStats] = useState({ total: 0, success: 0, failed: 0 });
  const [orders, setOrders] = useState([]);

  // Verificar estado del backend al cargar la app
  useEffect(() => {
    fetch('http://localhost:8080/api/orders/status')
      .then(res => res.json())
      .then(data => {
        setStatus(`Backend: ${data.backend} | RabbitMQ: ${data.rabbitmq}`);
      })
      .catch(() => {
        setStatus('Error de conexión con el backend');
      });
  }, []);

  const sendOrder = async (customerName) => {
    const orderId = `ORD-${Math.floor(1000 + Math.random() * 9000)}`;

    try {
      const response = await fetch('http://localhost:8080/api/orders/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          orderId: orderId,
          customerName: customerName,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        const newOrder = {
          id: data.orderId || orderId,
          customer: customerName,
          message: data.message,
          timestamp: new Date().toLocaleTimeString(),
          status: 'ENVIADA',
        };

        setOrders(prev => [newOrder, ...prev]);
        setStats(prev => ({ ...prev, total: prev.total + 1, success: prev.success + 1 }));
      } else {
        throw new Error('Error al enviar la orden');
      }
    // eslint-disable-next-line no-unused-vars
    } catch (error) {
      const failedOrder = {
        id: orderId,
        customer: customerName,
        message: 'No se pudo conectar con el servidor',
        timestamp: new Date().toLocaleTimeString(),
        status: 'FALLIDA',
      };

      setOrders(prev => [failedOrder, ...prev]);
      setStats(prev => ({ ...prev, total: prev.total + 1, failed: prev.failed + 1 }));
    }
  };

  return (
    <div className="App">
      <header className="header">
        <h1>Sistema de Órdenes con RabbitMQ</h1>
        <p className="status-badge">{status}</p>
      </header>

      <section className="control-panel">
        <h2>Enviar Órdenes</h2>
        <div className="button-group">
          <button onClick={() => sendOrder('Juan Pérez')} className="btn btn-primary">
            Orden Cliente 1
          </button>
          <button onClick={() => sendOrder('María García')} className="btn btn-primary">
            Orden Cliente 2
          </button>
          <button onClick={() => sendOrder('Carlos López')} className="btn btn-primary">
            Orden Cliente 3
          </button>
        </div>
      </section>

      <section className="stats-panel">
        <div className="stat-card">
          <div className="stat-number">{stats.total}</div>
          <div className="stat-label">Total Órdenes</div>
        </div>
        <div className="stat-card success">
          <div className="stat-number">{stats.success}</div>
          <div className="stat-label">Procesadas</div>
        </div>
        <div className="stat-card error">
          <div className="stat-number">{stats.failed}</div>
          <div className="stat-label">En DLQ / Fallidas</div>
        </div>
      </section>

      <section className="orders-list">
        <h2>Historial de Órdenes</h2>
        {orders.length === 0 ? (
          <p className="empty-message">No hay órdenes aún. ¡Envía una!</p>
        ) : (
          <div className="orders-table">
            {orders.map(order => (
              <div key={order.id} className={`order-item status-${order.status}`}>
                <div className="order-header">
                  <strong>{order.id}</strong>
                  <span className={`status-badge status-${order.status}`}>
                    {order.status}
                  </span>
                </div>
                <div className="order-detail">
                  <span>Cliente: {order.customer}</span>
                  <span className="timestamp">{order.timestamp}</span>
                </div>
                <div className="order-message">{order.message}</div>
              </div>
            ))}
          </div>
        )}
      </section>

      <footer className="footer">
        <p>Tip: Abre la consola de RabbitMQ (localhost:15672) para ver las colas en tiempo real</p>
      </footer>
    </div>
  );
}

export default App;