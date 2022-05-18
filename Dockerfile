FROM python:3.9

COPY ./ /usr/src

# Intall package feathr
WORKDIR /usr/src/feathr_project
RUN pip install -e .

# Install packages in requirements.txt
WORKDIR /usr/src/feathr_project/feathr/api
RUN pip install -r requirements.txt

# Start web server
CMD [ "uvicorn","app.main:app","--host", "0.0.0.0", "--port", "80" ]