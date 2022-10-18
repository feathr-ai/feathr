import React from "react";
import ReactDOM from "react-dom";
import App from "./app";
import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
import "antd/dist/antd.min.css";
import "./index.less";
import "./site.css";

dayjs.extend(utc);

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById("root")
);
