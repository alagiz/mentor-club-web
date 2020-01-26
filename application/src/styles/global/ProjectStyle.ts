import { createGlobalStyle } from "styled-components";

export const ProjectStyle = createGlobalStyle`
  h1 {
    font-size: 10px;
    margin-bottom: 10px;
  }
    
  body {
    .ant-popover-inner {
       border-radius: 0;
    }
        
    .ant-popover-inner-content {
       padding: 0;
    }
  }
`;
