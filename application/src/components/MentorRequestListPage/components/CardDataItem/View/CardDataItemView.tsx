import React from "react";
import { Styled } from "../Styled";
import { ICardDataItem } from "../Container/ICardDataItem";

const CardDataItemView: React.FC<ICardDataItem> = ({ value, label }) => {
  return (
    <Styled.CardDataItem>
      <Styled.CardDataItemLabel>{label}</Styled.CardDataItemLabel>
      <Styled.CardDataItemValue className={value.customClass}>
        {value.value}
      </Styled.CardDataItemValue>
    </Styled.CardDataItem>
  );
};

export default CardDataItemView;
