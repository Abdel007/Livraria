/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.AutorDAO;
import dao.LivroDAO;
import dao.ClassificacaoDAO;
import dao.EditoraDAO;
import dao.GeneroDAO;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Autor;
import modelo.Livro;
import modelo.Classificacao;
import modelo.Editora;
import modelo.Genero;

/**
 *
 * @author dappo
 */
@WebServlet(name = "LivroWS", urlPatterns = {"/admin/livro/LivroWS"})
public class LivroWS extends HttpServlet {
    private LivroDAO dao;
    private Livro obj;
    private String pagina;
    private String acao;
     
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        acao = request.getParameter("acao");
        List<Livro> lista = null;
        String id;
        switch(String.valueOf(acao)){
            case "del":
                id = request.getParameter("id");
                dao = new LivroDAO();
                pagina = "index.jsp";
                obj = dao.buscarPorChavePrimaria(Long.parseLong(id));
                Boolean deucerto = dao.excluir(obj);
                if(deucerto){   
                    lista = dao.listar();
                    request.setAttribute("lista", lista);
                    request.setAttribute("msg", "Excluído com sucesso");
                }
                else{
                    request.setAttribute("msg", "Erro ao excluir");
                }
                break;
            case "edit":
                id = request.getParameter("id");
                dao = new LivroDAO();
                Livro obj = dao.buscarPorChavePrimaria(Long.parseLong(id));
                request.setAttribute("obj", obj);
                pagina = "edita.jsp";
                break;
            default:
                dao = new LivroDAO();
                if (request.getParameter("filtro") != null) {
                    try {
                        lista = dao.listar(request.getParameter("filtro"));
                    } catch (Exception ex) {
                        Logger.getLogger(LivroWS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    lista = dao.listar();
                }
                //pra onde deve ser redirecionada a página
                pagina = "index.jsp";
                //passar a listagem para a página
                request.setAttribute("lista", lista);
                break;
                
        }
        RequestDispatcher destino = request.getRequestDispatcher(pagina);
        destino.forward(request, response);
        
    }
    
    public List<Genero> listarGeneros(){
                
            GeneroDAO dao = new GeneroDAO();
             List<Genero> lista = dao.listar();
            dao.fecharConexao();
            return lista;
            }
    
    public List<Autor> listarAutores(){
                
            AutorDAO dao = new AutorDAO();
             List<Autor> lista = dao.listar();
            dao.fecharConexao();
            return lista;
            }
    
    public List<Editora> listarEditoras(){
                
            EditoraDAO dao = new EditoraDAO();
             List<Editora> lista = dao.listar();
            dao.fecharConexao();
            return lista;
            }
    
    public List<Classificacao> listarClassificacoes(){
                
            ClassificacaoDAO dao = new ClassificacaoDAO();
             List<Classificacao> lista = dao.listar();
            dao.fecharConexao();
            return lista;
            }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String msg;
            request.setCharacterEncoding("UTF-8");
            //verificar campos obrigatórios
            if(request.getParameter("txtNome") == null){
                msg = "Campos obrigatórios não informados";
            }
            else{
                dao = new LivroDAO();
                obj = new Livro();
                //preencho o objeto com o que vem do post
                
                Boolean deucerto;
                
                Long Generos = Long.parseLong(request.getParameter("txtGenero"));
                Long Editoras = Long.parseLong(request.getParameter("txtUsuario"));
                Long Classificacoes = Long.parseLong(request.getParameter("txtClassificacao"));
                Long Autores = Long.parseLong(request.getParameter("txtAutor"));
                
                Genero genero;
                GeneroDAO generodao = new GeneroDAO();
                genero = generodao.buscarPorChavePrimaria(Generos);
                
                Editora editora;
                EditoraDAO editoradao = new EditoraDAO();
                editora = editoradao.buscarPorChavePrimaria(Editoras);
                
                Classificacao classificacao;
                ClassificacaoDAO classificacaodao = new ClassificacaoDAO();
                classificacao = classificacaodao.buscarPorChavePrimaria(Classificacoes);
                
                Autor autor;
                AutorDAO autordao = new AutorDAO();
                autor = autordao.buscarPorChavePrimaria(Autores);
                
                //se veio com a chave primaria então tem q alterar
                if(request.getParameter("txtId")!= null){
                    obj = dao.buscarPorChavePrimaria(Long.parseLong(request.getParameter("txtId")));
                    obj.setNome(request.getParameter("txtNome"));
                    obj.setAutor(autor);
                    obj.setClassificacao(classificacao);
                    obj.setEditora(editora);
                    obj.setGenero(genero);
                    
                    deucerto = dao.alterar(obj);
                    pagina="edita.jsp";
                }
                else{
                    obj.setNome(request.getParameter("txtNome"));
                    obj.setAutor(autor);
                    obj.setClassificacao(classificacao);
                    obj.setEditora(editora);
                    obj.setGenero(genero);
                    
                    deucerto = dao.incluir(obj);
                    pagina="add.jsp";   
                }
                if(deucerto){
                    msg = "Operação realizada com sucesso";    
                }
                else{
                    msg = "Erro ao realizar a operação";
                }
            }
            dao.fecharConexao();
            request.setAttribute("msg", msg);
            RequestDispatcher destino = request.getRequestDispatcher(pagina);
            destino.forward(request, response);
    }


}
