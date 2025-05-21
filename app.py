from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer

app = Flask(__name__)
model = SentenceTransformer('all-MiniLM-L6-v2')

@app.route('/get-embedding', methods=['POST'])
def get_embedding():
    data = request.get_json()
    text = data['text']
    embedding = model.encode(text).tolist()
    return jsonify({'embedding': embedding})

@app.route('/get-embeddings', methods=['POST'])
def get_embeddings():
    data = request.get_json()
    texts = data['texts']
    embeddings = model.encode(texts).tolist()
    return jsonify({'embeddings': embeddings})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
