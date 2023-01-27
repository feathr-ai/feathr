import React from 'react'

import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'
import ReactDOM from 'react-dom'

import App from './app'
import './site.css'

dayjs.extend(utc)

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
)
