import React from "react";
import CardDataItemView from "../View/CardDataItemView";
import { ICardDataItem } from "./ICardDataItem";

const CardDataItem: React.FC<ICardDataItem> = ({ value, label }) => {
  return <CardDataItemView label={label} value={value} />;
};

export default CardDataItem;
