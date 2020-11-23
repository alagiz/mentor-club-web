import React from "react";

export interface ICardDataItem {
  valueContainer: {
    value: string | React.ReactNode;
    customClass: string;
  };
  label: string;
}
