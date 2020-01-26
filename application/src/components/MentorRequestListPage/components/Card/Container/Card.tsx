import React from "react";
import CardView from "../View/CardView";
import { ICard } from "./ICard";

const Card: React.FC<ICard> = ({
  cardDataItems,
  cardBodyFooter,
  cardTitle
}) => {
  return (
    <CardView
      cardDataItems={cardDataItems}
      cardBodyFooter={cardBodyFooter}
      cardTitle={cardTitle}
    />
  );
};

export default Card;
