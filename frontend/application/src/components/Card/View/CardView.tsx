import React from "react";
import { Styled } from "../Styled";
import { ICard } from "../Container/ICard";
import CardDataItem from "../../CardDataItem/Container/CardDataItem";

const CardView: React.FC<ICard> = ({
  cardDataItems,
  cardBodyFooter,
  cardTitle,
  customClassName
}) => {
  const content = cardDataItems.map((cardDataItem, index) => (
    <CardDataItem
      label={cardDataItem.label}
      valueContainer={cardDataItem.valueContainer}
      key={`card-data-item-${index}`}
    />
  ));

  return (
    <Styled.AntdCard title={cardTitle} className={customClassName}>
      <Styled.AntdCardBody>{content}</Styled.AntdCardBody>
      <Styled.AntdCardBodyFooter>{cardBodyFooter}</Styled.AntdCardBodyFooter>
    </Styled.AntdCard>
  );
};

export default CardView;
