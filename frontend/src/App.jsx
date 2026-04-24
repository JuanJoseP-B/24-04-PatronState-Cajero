import { useState, useEffect } from 'react'
import './index.css'

const API_URL = 'http://localhost:8080/api/atm';

function App() {
  const [atmState, setAtmState] = useState({
    currentState: 'IdleState',
    message: 'Bienvenido. Por favor, inserte su tarjeta.',
    success: true,
    balance: 0
  });
  const [logs, setLogs] = useState([]);
  const [pin, setPin] = useState('');
  const [loading, setLoading] = useState(false);
  const [showIntro, setShowIntro] = useState(true);

  const addLog = (msg) => {
    const timestamp = new Date().toLocaleTimeString();
    setLogs(prev => [`[${timestamp}] ${msg}`, ...prev]);
  };

  const fetchStatus = async () => {
    try {
      const res = await fetch(`${API_URL}/status`);
      const data = await res.json();
      setAtmState(data);
    } catch (err) {
      console.error("Error fetching status", err);
    }
  };

  const handleAction = async (endpoint, body = null) => {
    setLoading(true);
    try {
      const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      };
      if (body) options.body = JSON.stringify(body);

      const res = await fetch(`${API_URL}/${endpoint}`, options);
      const data = await res.json();
      
      const prevClass = atmState.currentState;
      setAtmState(data);
      
      if (data.currentState !== prevClass) {
        addLog(`SISTEMA: Transición de ${prevClass} a ${data.currentState} exitosa.`);
      }
      
      if (!data.success) {
        addLog(`ERROR: ${data.message}`);
      }

      if (endpoint === 'enter-pin') setPin('');
    } catch (err) {
      addLog("ERROR: No se pudo conectar con el servidor.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStatus();
    addLog("SISTEMA: Conectado al Backend.");
    
    // Polling every 2 seconds to keep state in sync (Source of Truth)
    const interval = setInterval(fetchStatus, 2000);
    return () => clearInterval(interval);
  }, []);

  const isNumpadActive = atmState.currentState === 'PinValidationState' || atmState.currentState === 'CardInsertedState';
  const isWithdrawalActive = atmState.currentState === 'TransactionState';

  return (
    <>
      {showIntro && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Bienvenido a StateCash</h2>
            <p>Este proyecto demuestra la implementación del <strong>Patrón de Diseño State</strong> en un entorno de Microservicios.</p>
            
            <h3>🚀 ¿Cómo funciona el proyecto?</h3>
            <ul>
              <li><strong>Backend (Java):</strong> Es el "Dueño de la Verdad". Gestiona los estados y transiciones.</li>
              <li><strong>Frontend (React):</strong> Es una interfaz reactiva que solo refleja lo que el servidor ordena.</li>
            </ul>

            <h3>💳 Procedimiento de Uso</h3>
            <ol style={{paddingLeft: '20px'}}>
              <li>Haz clic en <strong>"Insertar Tarjeta"</strong> para iniciar.</li>
              <li>Ingresa el PIN de seguridad <strong>(Clave: 1234)</strong> y presiona <strong>OK</strong>.</li>
              <li>Selecciona un monto para retirar dinero.</li>
            </ol>

            <h3>⚠️ Manejo de PIN Incorrecto</h3>
            <p>Si ingresas una clave distinta a 1234, el backend rechazará la transacción, enviando un mensaje de error y manteniendo el estado de validación. Podrás ver este evento en tiempo real en el <strong>Panel del Profesor</strong>.</p>

            <div className="modal-footer">
              <button className="btn btn-primary btn-large" onClick={() => setShowIntro(false)}>
                ENTRAR AL SISTEMA STATECASH →
              </button>
            </div>
          </div>
        </div>
      )}

      <header className="header">
        <h1>STATECASH</h1>
        <div style={{fontSize: '0.8rem', opacity: 0.7}}>ATM DESIGN PATTERN SIMULATOR</div>
      </header>

      <main className="main-container">
        {/* Lado Izquierdo: Simulador */}
        <section className="simulator-card">
          <div className="atm-screen">
            <div style={{color: '#ff0', marginBottom: '10px', fontSize: '0.8rem'}}>SALDO DISPONIBLE: ${atmState.balance.toLocaleString()}</div>
            <h2>{atmState.message}</h2>
            {atmState.currentState === 'PinValidationState' && (
              <div style={{fontSize: '2rem', letterSpacing: '10px'}}>
                {pin.split('').map(() => '*').join('')}
              </div>
            )}
          </div>

          <div className="controls-grid">
            <div className="numpad">
              {[1, 2, 3, 4, 5, 6, 7, 8, 9, 'C', 0, 'OK'].map((key) => (
                <button 
                  key={key} 
                  className="btn btn-num"
                  disabled={!isNumpadActive || loading}
                  onClick={() => {
                    if (key === 'C') setPin('');
                    else if (key === 'OK') handleAction('enter-pin', { pin });
                    else if (pin.length < 4) setPin(prev => prev + key);
                  }}
                >
                  {key}
                </button>
              ))}
            </div>

            <div style={{display: 'flex', flexDirection: 'column', gap: '10px'}}>
              <button 
                className="btn btn-primary" 
                disabled={atmState.currentState !== 'IdleState' || loading}
                onClick={() => handleAction('insert-card')}
              >
                Insertar Tarjeta
              </button>
              <button 
                className="btn btn-primary"
                disabled={atmState.currentState === 'IdleState' || loading}
                onClick={() => handleAction('eject-card')}
              >
                Expulsar Tarjeta
              </button>
              
              {isWithdrawalActive && (
                <div style={{marginTop: '20px', display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px'}}>
                  <button className="btn btn-primary" onClick={() => handleAction('withdraw', {amount: 20000})}>$20.000</button>
                  <button className="btn btn-primary" onClick={() => handleAction('withdraw', {amount: 50000})}>$50.000</button>
                </div>
              )}
            </div>
          </div>
        </section>

        {/* Lado Derecho: Panel del Profesor */}
        <section className="teacher-panel">
          <div className="panel-section">
            <div className="panel-title">Monitor de Lógica (Backend)</div>
            <div style={{marginBottom: '10px'}}>
              <strong>Active Class:</strong><br/>
              <span className="class-name">com.atm.logic.state.{atmState.currentState}</span>
            </div>
          </div>

          <div className="panel-section" style={{flex: 1}}>
            <div className="panel-title">Registro de Eventos (Transitions)</div>
            <div className="logs-container">
              {logs.map((log, i) => (
                <div key={i} className="log-entry">
                  <span className="log-system">{log.split(' ')[1]}</span> {log.split(' ').slice(2).join(' ')}
                </div>
              ))}
            </div>
          </div>

          <button className="btn" style={{background: '#333', color: 'white'}} onClick={() => handleAction('reset').then(() => window.location.reload())}>
            REINICIAR SIMULACIÓN
          </button>
        </section>
      </main>
    </>
  )
}

export default App
