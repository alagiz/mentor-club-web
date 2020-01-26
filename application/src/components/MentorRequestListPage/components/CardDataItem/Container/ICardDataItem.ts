import React from "react";

export interface ICardDataItem {
  value: {
    value: string | React.ReactNode;
    customClass: string;
  };
  label: string;
}
