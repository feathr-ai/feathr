import { Card, Typography } from 'antd'
import { useParams } from 'react-router-dom'

const { Title } = Typography

const ResponseErrors = () => {
  const { status, detail } = useParams()
  switch (status) {
    case '403':
      return (
        <div className="page">
          <Card>
            <Title level={3}> ERROR 403</Title>
            ACCESS NOT GRANTED
            <Card>{detail}</Card>
          </Card>
        </div>
      )

    default:
      return (
        <div className="page">
          <Card>
            <Title level={3}>Unknown Error</Title>
          </Card>
        </div>
      )
  }
}

export default ResponseErrors
