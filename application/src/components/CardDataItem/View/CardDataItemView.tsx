import React from "react";
import { Styled } from "../Styled";
import { ICardDataItem } from "../Container/ICardDataItem";

const CardDataItemView: React.FC<ICardDataItem> = ({
  valueContainer,
  label
}) => {
  return (
    <Styled.CardDataItem>
      <Styled.CardDataItemLabel>{label}</Styled.CardDataItemLabel>
      <Styled.CardDataItemValue className={valueContainer.customClass}>
        {valueContainer.value}
      </Styled.CardDataItemValue>
    </Styled.CardDataItem>
  );
};

export default CardDataItemView;
