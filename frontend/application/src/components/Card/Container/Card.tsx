import React from "react";
import CardView from "../View/CardView";
import { ICard } from "./ICard";

const Card: React.FC<ICard> = ({
  cardDataItems,
  cardBodyFooter,
  cardTitle,
  customClassName
}) => {
  return (
    <CardView
      cardDataItems={cardDataItems}
      cardBodyFooter={cardBodyFooter}
      cardTitle={cardTitle}
      customClassName={customClassName}
    />
  );
};

export default Card;
