const express = require('express');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 4200;
const distFolder = path.join(__dirname, 'dist', 'gesthor-angular', 'browser');

app.use(express.static(distFolder));

app.get('/*', function (req, res) {
  res.sendFile(path.join(distFolder, 'index.html'));
});

app.listen(PORT, () => {
  console.log(`Servidor ejecutándose en http://localhost:${PORT}`);
});
