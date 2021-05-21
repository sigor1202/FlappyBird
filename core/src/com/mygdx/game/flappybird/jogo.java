package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class jogo extends ApplicationAdapter {


	//cria as variaveis e a lista
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float posicaoInicialVerticalPassaro = 0;
	private float variacao = 0;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;



	private int pontos = 0;
	private float gravidade = 0;

	Texture[] passaros;
	Texture fundo;
	Texture canoBaixo;
	Texture canoTopo;
	SpriteBatch batch;
	BitmapFont textoPontuacao;
	private boolean passouCano = false;
	private Random random;

	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	@Override
	//classe semelhante ao onCreate
	public void create () {

		inicializaTexturas();
		inicializaObjetos();


	}

	private void inicializaObjetos() {
		random = new Random();
		batch = new SpriteBatch();

		//pega a altura e largura da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo/2;
		posicaoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 350;

		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);

		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoCima = new Rectangle();
		retanguloCanoBaixo = new Rectangle();


	}

	private void inicializaTexturas() {
		//cria uma lista de objetos passaros e atribui os tres a ela
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//pega a imagem fundo e atribui a variavel
		fundo = new Texture("fundo.png");
		//pega as texturas dodos canos e atreibui a variavel
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
	}

	@Override
	//renderiza o conteudo
	public void render () {

		verificaEstadojogo();
		desenharTexturas();
		detectarColisao();
		validaPontos();

	}

	private void detectarColisao() {
		//cria os colliders
		circuloPassaro.set(50 + passaros[0].getWidth()/2,
				posicaoInicialVerticalPassaro+passaros[0].getHeight()/2,passaros[0].getHeight());

		retanguloCanoBaixo.set(posicaoCanoHorizontal,
				alturaDispositivo/2-canoBaixo.getHeight()-espacoEntreCanos/2+posicaoCanoVertical,canoBaixo.getWidth(),
				canoBaixo.getHeight());

		retanguloCanoCima.set(posicaoCanoHorizontal,
				alturaDispositivo/2-canoBaixo.getHeight()-espacoEntreCanos/2+posicaoCanoVertical,
				canoBaixo.getWidth(),canoBaixo.getHeight());
			//cria os booleans para verificar a colisão
			boolean bateuCanoCima = Intersector.overlaps(circuloPassaro,retanguloCanoCima);
			boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro,retanguloCanoBaixo);

			//verifica se bateu
			if(bateuCanoBaixo || bateuCanoCima){
				Gdx.app.log("tag","bateu");
			}


	}

		//verifica se passou do cano se sim adiciona mais um ao pontos
	private void validaPontos() {
		if (posicaoCanoHorizontal<50-passaros[0].getWidth())
		{
			if(!passouCano)
			{
				pontos++;
				passouCano = true;
			}
		}
	}

	private void verificaEstadojogo() {
		posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime()*200;
		if (posicaoCanoHorizontal < - canoBaixo.getHeight()){
			posicaoCanoHorizontal = larguraDispositivo;
			posicaoCanoVertical = random.nextInt(400)-200;
			passouCano = false;
		}

		//verificação do clique na tela
		boolean toquTela = Gdx.input.justTouched();

		//se flor clicado subtrai 25 da gravidade
		if(Gdx.input.justTouched())
		{
			gravidade = -25;
		}

		//verifica se a posição vertical é maior que zero ou clicou na tela
		if (posicaoInicialVerticalPassaro > 0 || toquTela) {
			//atualiza a posição vertical
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
		}

		variacao += Gdx.graphics.getDeltaTime() * 10;
		//verifica se a variação e se for maior que tres iguala a zero
		if(variacao > 3)
			variacao = 0;

		//adiciona +1 nas variaveis
		gravidade++;

	}

	private void desenharTexturas() {
		batch.begin();

		//desenha e configura o onbeto na tela
		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],50,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo/2- canoBaixo.getHeight()-espacoEntreCanos/2+ posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo/2 + espacoEntreCanos/2 + posicaoCanoVertical);
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo/2,alturaDispositivo-100);

		batch.end();
	}

	@Override
	public void dispose () {

	}
}
