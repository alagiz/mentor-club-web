import { createGlobalStyle } from "styled-components";

export const ProjectStyle = createGlobalStyle`
  h1 {
    font-size: 10px;
    margin-bottom: 10px;
  }
    
  body {
    #root > * {
      flex: 1 100%;
    }
    
    .ant-popover-inner, .ant-select-dropdown {
       border-radius: 0;
    }
        
    .ant-popover-inner-content {
       padding: 0;
    }
  }
`;
