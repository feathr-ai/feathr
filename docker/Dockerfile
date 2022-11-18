FROM python:3.9

ARG CODE_SERVER_VERSION=2.1692-vsc1.39.2
ENV CODE_SERVER_AUTH=none \
    CODE_SERVER_PASSWORD=feathr \ 
    JUPYTER_TOKEN=feathr

# install enssential libs
RUN apt-get update && \
    apt-get install -y \
        wget curl git cmake


# install useful tools: sshd, jupyterlab and so on...
RUN apt-get update && \
    apt-get install -y supervisor vim sudo zip unzip htop && \
    pip install --no-cache-dir jupyterlab==0.35.4 pandavro

RUN mkdir /feathr
WORKDIR /feathr
RUN pip install git+https://github.com/feathr-ai/feathr.git#subdirectory=feathr_project

# install code-server
RUN mkdir -p /opt/code-server && \
    wget -qO- https://github.com/cdr/code-server/releases/download/${CODE_SERVER_VERSION}/code-server${CODE_SERVER_VERSION}-linux-x86_64.tar.gz | tar -xzf - --strip=1 -C /opt/code-server


# copy supervisord configuration and mkdir
COPY supervisord.conf /opt/supervisord.conf
RUN mkdir -p /opt/logs

# 8080: VsCode, 9090: jupyter
EXPOSE 8080 9090

CMD ["supervisord", "-c", "/opt/supervisord.conf"]