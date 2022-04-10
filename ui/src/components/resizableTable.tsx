import React, { PureComponent } from 'react';
import { Table } from 'antd';
import { Resizable } from 'react-resizable';

const ResizableTitle = (props: { [x: string]: any; onResize: any; width: any; }) => {
  const { onResize, width, ...restProps } = props;
  if (!width) {
    return <th { ...restProps } />
  }
  return (
    <Resizable width={ width } height={ 0 } onResize={ onResize } draggableOpts={ { enableUserSelectHack: false } }>
      <th { ...restProps } />
    </ Resizable>
  )
}

class TableResize extends PureComponent<any, any> {

  state = {
    columns: this.props.columns
  };

  components = {
    header: {
      cell: ResizableTitle,
      userSelect: "none"
    },
  };

  handleResize = (index: number) => (e: any, { size }: any) => {
    this.setState(({ columns }: any) => {
      const nextColumns = [...columns];
      nextColumns[index] = {
        ...nextColumns[index],
        width: size.width,
      };
      return { columns: nextColumns };
    });
  };

  render() {
    const columns = this.state.columns.map((col: any, index: number) => ({
      ...col,
      onHeaderCell: (column: { width: any; }) => ({
        width: column.width,
        onResize: this.handleResize(index),
      }),
    }));

    return <Table
      components={ this.components }
      columns={ columns }
      rowKey="id"
      dataSource={ this.props.dataSource }
      loading={ this.props.loading }
    />
  }
}

export default TableResize
