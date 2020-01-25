import React from "react";
import { mapStateToProps } from "./PrivateRoute";

export type IPrivateRouteProps = {
  children?: React.ReactNode;
} & ReturnType<typeof mapStateToProps>;
