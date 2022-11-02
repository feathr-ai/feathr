import React from "react";
import { Resizable } from "react-resizable";
import ResizeHandle from "./ResizeHandle";
import { ResizableTitleProps } from "./interface";

import styles from "./index.module.less";

const ResizableTitle = (props: ResizableTitleProps) => {
  const { onResize, width, minWidth, ...restProps } = props;

  if (!width) {
    return <th {...restProps} />;
  }

  return (
    <Resizable
      width={width}
      height={0}
      handle={<ResizeHandle />}
      onResize={onResize}
      minConstraints={minWidth ? [minWidth, minWidth] : undefined}
      onResizeStart={() => {
        document.body.classList.add(styles.colResize);
      }}
      onResizeStop={() => {
        document.body.classList.remove(styles.colResize);
      }}
      draggableOpts={{
        enableUserSelectHack: false,
      }}
    >
      <th {...restProps} />
    </Resizable>
  );
};

export default ResizableTitle;
