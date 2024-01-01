import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { HashRouter, Route, Routes } from 'react-router-dom';
import { Counter } from './Pages/Counter';
import { Feed } from './Pages/Home';
import { Register } from './Pages/Register';
import { Create } from './Pages/Create';
import { Post } from './Pages/Post';
import { UpdatePost } from './Pages/Update';

function App() {
  return (
    <div>
      <Counter/>
      <HashRouter >
        <Routes>
          <Route path='/' element={<Feed />} />
          <Route path='/register' element={<Register />} />
          <Route path='/post' element={<Create />} />
          <Route path='/update/:id' element={<UpdatePost/>} />
          <Route path='/user/:id' element={<Post/>} />

        </Routes>
      </HashRouter>
    </div>

  );
}

export default App;
