import React from "react";
import CardDataItemView from "../View/CardDataItemView";
import { ICardDataItem } from "./ICardDataItem";

const CardDataItem: React.FC<ICardDataItem> = ({ valueContainer, label }) => {
  return <CardDataItemView label={label} valueContainer={valueContainer} />;
};

export default CardDataItem;
