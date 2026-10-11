require('dotenv').config();
const express = require('express');
const cors = require('cors');
const path = require('path');
const { initDatabase } = require('./db');
const doctorRoutes = require('./routes/doctorRoutes');
const adminRoutes = require('./routes/adminRoutes');

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ extended: true, limit: '50mb' }));

// Serve static frontend assets from /public
const publicDir = path.join(__dirname, '../public');
app.use(express.static(publicDir));

// Mount API routes
app.use('/api', doctorRoutes);
app.use('/api', adminRoutes);

// Protected or direct admin page route
app.get('/admin', (req, res) => {
  res.sendFile(path.join(publicDir, 'admin.html'));
});

// Explicit home page route
app.get('/', (req, res) => {
  res.sendFile(path.join(publicDir, 'index.html'));
});

// Centralized error handler
app.use((err, req, res, next) => {
  console.error('Unhandled server error:', err);
  res.status(500).json({
    success: false,
    message: 'حدث خطأ داخلي في الخادم: ' + (err.message || 'خطأ غير معروف')
  });
});

initDatabase()
  .then(() => {
    console.log('✅ SQLite Database initialized successfully.');
    app.listen(PORT, '0.0.0.0', () => {
      console.log(`🚀 دليل أطباء البصرة — Backend running on http://0.0.0.0:${PORT}`);
    });
  })
  .catch((err) => {
    console.error('❌ Failed to initialize database:', err);
    process.exit(1);
  });
