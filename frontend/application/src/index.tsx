import React from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import "antd/dist/antd.css";
import { ProjectStyle } from "./styles/global/ProjectStyle";
import { store } from "./store";
import App from "./components/App/Container/App";

ReactDOM.render(
  <Provider store={store}>
    <ProjectStyle />
    <App />
  </Provider>,
  document.getElementById("root")
);
