package restAssuredLab.testes;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.expect;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

public class UsuariosWSTest {
	
	private Usuario esperado1;
	private Usuario esperado2;
	private Leilao leilaoEperado;


	@Before
	public void setUp() {
		esperado1 = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		esperado2 = new Usuario(2L, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");
		leilaoEperado = new Leilao(1L, "Geladeira", 800.00, esperado1, false);

	}

	@Test
	public void deveRetornarListadeUsuariosJSONPeloID() {
		JsonPath jsonPath = given()
				.header("Accept", "application/json")
				.queryParam("usuario.id", 1)
				.get("/usuarios/show")
				.andReturn().jsonPath();
		
		Usuario userRetornado =  jsonPath.getObject("usuario", Usuario.class);
		
		assertEquals(esperado1, userRetornado);
		
	}
	
	
	@Test
	public void deveRetornarListadeLeiloesJSONID() {
		Response response = given()
				.header("Accept", "application/json")
				.queryParam("leilao.id", 1)
				.get("/leiloes/show")
				.andReturn();

		JsonPath jsonPath = response.jsonPath();
		
		Leilao leilaoRetornado = jsonPath.getObject("leilao", Leilao.class);
		
		assertEquals(leilaoEperado, leilaoRetornado);
		
	}
	
	
	@Test
    public void deveRetornarListaDeUsuariosJSON() {
        Response response = given()
                .header("Accept", "application/json")
                .get("/usuarios");
        
        JsonPath path = response.jsonPath();
        
        System.out.println();
        
        List<Usuario> usuarios = path.getList("list", Usuario.class);
        
        assertEquals(esperado1, usuarios.get(0));
        assertEquals(esperado2, usuarios.get(1));

    }
	
	
	@Test
	public void deveRetornarQtdleiloesXML() {
		 //XmlPath path = get("/usuarios?_format=xml").andReturn().xmlPath();
		XmlPath path = given()
                .header("Accept", "application/xml")
                .get("/leiloes/total")
                .andReturn()
                .xmlPath();
		
		int valor = path.getInt("int");
		
		assertEquals(2, valor);

	}



	@Test
	public void deveAdicionarUmUsuario(){

		Usuario joao = new Usuario("Joao da Silva","joao@dasilva.com");

		XmlPath retorno =
				given()
						.header("Accept", "application/xml")
						.contentType("application/xml")
						.body(joao)
						.expect()
						.statusCode(200)
						.when()
						.post("/usuarios")
						.andReturn()
						.xmlPath();

		Usuario resposta = retorno.getObject("usuario", Usuario.class);

		assertEquals("Joao da Silva", resposta.getNome());
		assertEquals("joao@dasilva.com", resposta.getEmail());




	}

	@Test
	public void deveGerarUmCookie() {
		expect()
				.cookie("rest-assured", "funciona")
				.when()
				.get("/cookie/teste");
	}

	@Test
	public void deveGerarUmHeader() {
		expect()
				.header("novo-header", "abc")
				.when()
				.get("/cookie/teste");
	}
	
	
}
