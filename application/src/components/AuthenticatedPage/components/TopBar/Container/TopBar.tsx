import React from "react";
import TopBarView from "../View/TopBarView";
import { ITopBar } from "./ITopBar";

const TopBar: React.FC<ITopBar> = ({ logout }) => {
  return <TopBarView logout={logout} />;
};

export default TopBar;
