import axios from 'axios';

// URL base do backend (Spring Boot)
const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

export default api;