export interface IFieldDropdownProps {
  labelText: string;
  optionValues: any[];
  loading?: boolean;
  value?: any | null;
  onChange: (value: any) => void;
  defaultValue?: any;
}
