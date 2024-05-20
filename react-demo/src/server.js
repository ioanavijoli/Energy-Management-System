const express = require('express');
const cors = require('cors');
const app = express();
const port = 8080;

app.use(cors());

// Define your server routes and logic here

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
