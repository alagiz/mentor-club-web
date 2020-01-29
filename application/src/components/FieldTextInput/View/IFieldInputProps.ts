import { ChangeEvent } from "react";

export interface IFieldInputProps {
  labelText: string;
  onChange: (
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => void;
  value?: string;
  isTextArea?: boolean;
  numberOfLinesForTextArea?: number;
}
