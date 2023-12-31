import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { HashRouter, Route, Routes } from 'react-router-dom';
import { Feed } from './Pages/Home';
import { Register } from './Pages/Register';
import { Create } from './Pages/Create';
import { Post } from './Pages/Post';
import { UpdatePost } from './Pages/Update';
import { SinglePost } from './Pages/SinglePost';

function App() {
  return (
    <div>
      {/*<Counter />*/}
      <HashRouter >
        <Routes>
          <Route path='/' element={<Feed />} />
          <Route path='/register' element={<Register />} />
          <Route path='/post' element={<Create />} />
          <Route path='/update/:id' element={<UpdatePost />} />
          <Route path='/user/:id' element={<Post />} />
          <Route path='/comment/:id' element={<SinglePost />} />
        </Routes>
      </HashRouter>
    </div>

  );
}

export default App;
