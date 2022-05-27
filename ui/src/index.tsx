import React from 'react';
import ReactDOM from 'react-dom';
import Routes from './router/routes';
import 'antd/dist/antd.min.css';
import './index.less';
import "./site.css"

ReactDOM.render(
  <React.StrictMode>
    <Routes />
  </React.StrictMode>,
  document.getElementById('root'));


