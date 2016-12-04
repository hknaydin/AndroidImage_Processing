package com.hakan.androidimageprocessing;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Etiketleme {
	public static double kullanýlanTDegeri;
	static int[][] dizika = null;// resmin içindeki nesneleri etiketledim yani
									// sayýsal
	// deðerler verdim
	static ArrayList<Integer> dizikaSayisi = new ArrayList<Integer>();// etikette

	// kullanýlan
	// sayýlarýn

	// deðerlerini tuttum

	public static Bitmap convert_Binary(Bitmap src) {

		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				src.getConfig());

		int R, G, B, A, T;
		int pixelColor;
		int height = src.getHeight();
		int width = src.getWidth();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				pixelColor = src.getPixel(x, y);
				A = Color.alpha(pixelColor);
				R = Color.red(pixelColor);
				G = Color.green(pixelColor);
				B = Color.blue(pixelColor);
				T = (int) (R * 0.3 + G * 0.59 + B * 0.11);
				if (T >= 155) // eþik degerini 128 olarak aldým.
					T = 255; // beyaz yapar.
				else
					T = 0; // siyah yapar.
				// set newly-inverted pixel to output image
				bmOut.setPixel(x, y, Color.rgb(T, T, T));
			}
		}

		return bmOut;

	}

	public static Bitmap convert_grey(Bitmap source) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();

		Bitmap bmOut = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), source.getConfig());

		int R, G, B;
		// iteration through pixels
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int pixelColor = source.getPixel(x, y);
				R = Color.red(pixelColor);
				G = Color.green(pixelColor);
				B = Color.blue(pixelColor);
				// set newly-inverted pixel to output image
				int T = (int) (R * 0.59 + G * 0.3 + B * 0.11);
				bmOut.setPixel(x, y, Color.rgb(T, T, T));
			}
		}

		return bmOut;
	}

	public static Bitmap binaryresimYapmaT2(Bitmap source) {
		int width = source.getWidth();
		int height = source.getHeight();

		Bitmap bmOut = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), source.getConfig());
		double T1, T2;
		int R, G, B;
		Random r = new Random();
		do {
			T1 = (r.nextInt(256));
			T2 = (r.nextInt(256));

		} while (T1 == T2);

		while (true) {
			int topT1icin = 0, topT2icin = 0, sayT1icin = 0, sayT2icin = 0;
			double a, b, T13, T24;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixelColor = source.getPixel(x, y);
					R = Color.red(pixelColor);
					G = Color.green(pixelColor);
					B = Color.blue(pixelColor);
					int Y = (int) (R * 0.3 + G * 0.59 + B * 0.11);
					a = Math.abs((double) Y - T1);
					b = Math.abs((double) Y - T2);
					if (a <= b) {
						topT1icin += Y;
						sayT1icin++;
					} else {
						topT2icin += Y;
						sayT2icin++;
					}
				}
			}
			T13 = ((double) topT1icin) / sayT1icin;
			T24 = ((double) topT2icin) / sayT2icin;
			if (Math.abs(T13 - T1) < 0.001 && Math.abs(T24 - T2) < 0.001) {
				break;
			} else {
				T1 = T13;
				T2 = T24;
			}
		}
		double T = (T1 + T2) / 2;

		kullanýlanTDegeri = T;
		int R1, G1, B1;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixelColor = source.getPixel(x, y);
				R1 = Color.red(pixelColor);
				G1 = Color.green(pixelColor);
				B1 = Color.blue(pixelColor);
				int Y = (int) (R1 * 0.3 + G1 * 0.59 + B1 * 0.11);
				if (T < Y)
					bmOut.setPixel(x, y, Color.rgb(255, 255, 255));
				else
					bmOut.setPixel(x, y, Color.rgb(0, 0, 0));
			}
		}

		return bmOut;
	}

	public static Bitmap etiketlemeYapma(Bitmap resim) {
		dizika = new int[resim.getHeight()][resim.getWidth()];
		for (int r = 0; r < resim.getHeight(); r++)
			for (int c = 0; c < resim.getWidth(); c++)
				dizika[r][c] = 0;
		int k = 65; // 65'ten itibaren atama yapýlacak
		int sma = k;
		// //////////////////////////////////siyah renk pixellerin yerleri
		// belirlenmeye
		// çalýþýlýyor//////////////////////////////////////////////////
		for (int r = 0; r < resim.getHeight(); r++) {
			for (int c = 0; c < resim.getWidth(); c++) {
				int pixelColor = resim.getPixel(c, r);
				int R, G, B;
				R = Color.red(pixelColor);
				G = Color.green(pixelColor);
				B = Color.blue(pixelColor);
				int Y = (int) (R * 0.3 + G * 0.59 + B * 0.11);
				if (Y == 0) {
					if (r == 0 && c == 0) {
						dizika[r][c] = k;
						k++;
					} // ///atama
					else if (r == 0 && c != 0) {
						if (dizika[r][c - 1] != 0) {
							dizika[r][c] = dizika[r][c - 1];
						} // sol taraftan atama
						else {
							dizika[r][c] = k;
							k++;
						} // ///atama
					} else if (r != 0 && c == 0) {
						if (dizika[r - 1][c] != 0) {
							dizika[r][c] = dizika[r - 1][c];
						} // yukarýdan atama
						else if (dizika[r - 1][c + 1] != 0) {
							dizika[r][c] = dizika[r - 1][c + 1];
						} // kuzeydoðudan atama
						else {
							dizika[r][c] = k;
							k++;
						}// ///atama
					} else if (r != 0 && c != (resim.getWidth() - 1)) {
						if (dizika[r][c - 1] != 0) {
							dizika[r][c] = dizika[r][c - 1];
						}// arka yanýndan atama
						else if (dizika[r - 1][c - 1] != 0) {
							dizika[r][c] = dizika[r - 1][c - 1];
						} // kuzeybatýdan atama
						else if (dizika[r - 1][c] != 0) {
							dizika[r][c] = dizika[r - 1][c];
						} // yukarýdan atama
						else if (dizika[r - 1][c + 1] != 0) {
							dizika[r][c] = dizika[r - 1][c + 1];
						} // kuzeydoðudan atama
						else {
							dizika[r][c] = k;
							k++;
						}// ///atama
					} else if (r != 0 && c == (resim.getWidth() - 1)) {
						if (dizika[r][c - 1] != 0) {
							dizika[r][c] = dizika[r][c - 1];
						}// arka yanýndan atama
						else if (dizika[r - 1][c - 1] != 0) {
							dizika[r][c] = dizika[r - 1][c - 1];
						}// kuzeybatýdan atama
						else if (dizika[r - 1][c] != 0) {
							dizika[r][c] = dizika[r - 1][c];
						} // yukarýdan atama
						else {
							dizika[r][c] = k;
							k++;
						}
					} else if (r == (resim.getHeight() - 1) && c == 0) {
						if (dizika[r - 1][c] != 0) {
							dizika[r][c] = dizika[r - 1][c];
						} // yukarýdan atama
						else if (dizika[r - 1][c + 1] != 0) {
							dizika[r][c] = dizika[r - 1][c + 1];
						} // kuzeydoðudan atama
						else {
							dizika[r][c] = k;
							k++;
						}
					}
				}
			}
		}

		// /////////////////////////////////////////birbirine temas halinde olan
		// siyah pixeller ayný gruba alýnmaya
		// çalýþýlýyor/////////////////////////////////////
		int n, m, aranan, koyulacakolan;
		for (int r = 0; r < resim.getHeight(); r++) {
			for (int c = 0; c < resim.getWidth(); c++) {
				n = dizika[r][c];
				if (n != 0) {
					if (r != (resim.getHeight() - 1) && c == 0) {
						if (n != dizika[r][c + 1] && dizika[r][c + 1] != 0) {// sað
																				// taraf
							k--;
							m = dizika[r][c + 1];
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						}

						else if (n != dizika[r + 1][c] && dizika[r + 1][c] != 0) {// aþaðýya
							m = dizika[r + 1][c];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						} else if (n != dizika[r + 1][c + 1]
								&& dizika[r + 1][c + 1] != 0) {// guneydoðu
							m = dizika[r + 1][c + 1];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						}

					} else if (r != (resim.getHeight() - 1)
							&& c != (resim.getWidth() - 1)) {
						if (n != dizika[r][c + 1] && dizika[r][c + 1] != 0) {// sað
																				// taraf
							m = dizika[r][c + 1];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						}

						else if (n != dizika[r + 1][c] && dizika[r + 1][c] != 0) {// aþaðýya
							m = dizika[r + 1][c];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						} else if (n != dizika[r + 1][c + 1]
								&& dizika[r + 1][c + 1] != 0) {// guneydoðu
							m = dizika[r + 1][c + 1];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						} else if (c != 0 && n != dizika[r + 1][c - 1]
								&& dizika[r + 1][c - 1] != 0) {// guneybatý
							m = dizika[r + 1][c - 1];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						}
					} else if (r != (resim.getHeight() - 1)
							&& c == (resim.getWidth() - 1)) {
						if (n != dizika[r + 1][c] && dizika[r + 1][c] != 0) {// aþaðýya
							m = dizika[r + 1][c];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						} else if (c != 0 && n != dizika[r + 1][c - 1]
								&& dizika[r + 1][c - 1] != 0) {// guneybatý
							m = dizika[r + 1][c - 1];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						}
					} else if (r == (resim.getHeight() - 1)
							&& c != (resim.getWidth() - 1)) {
						if (n != dizika[r][c + 1] && dizika[r][c + 1] != 0) {// sað
																				// taraf
							m = dizika[r][c + 1];
							k--;
							if (n < m) {
								aranan = m;
								koyulacakolan = n;
							} else {
								aranan = n;
								koyulacakolan = m;
							}
							for (int w = 0; w < resim.getHeight(); w++) {
								for (int s = 0; s < resim.getWidth(); s++) {
									if (aranan == dizika[w][s])
										dizika[w][s] = koyulacakolan;
								}
							}
						}
					}
				}
			}
		}

		for (int r = 0; r < resim.getHeight(); r++) {
			for (int c = 0; c < resim.getWidth(); c++) {
				if (!(dizikaSayisi.contains(dizika[r][c])) && dizika[r][c] != 0) {
					dizikaSayisi.add(dizika[r][c]);// global yap.
				}
			}
		}

		// //////////////////////////////////////////Bulunan nesneler
		// renklendirilecek//////////////////////////////////////////////////////////////////////
		ArrayList Renk = new ArrayList();
		Random rastgeleSayý = new Random();
		int yenirenk, yenirenk1, yenirenk2, sayac = 0;
		int nesneSayýsý = k - sma;

		while (sayac < nesneSayýsý) {
			do {
				do {
					yenirenk = rastgeleSayý.nextInt(256);
					yenirenk1 = rastgeleSayý.nextInt(256);
					yenirenk2 = rastgeleSayý.nextInt(256);
				} while (yenirenk == yenirenk1 && yenirenk == yenirenk2);
			} while (Renk.contains(yenirenk * 1000000 + yenirenk1 * 1000
					+ yenirenk2));

			Renk.add(yenirenk * 1000000 + yenirenk1 * 1000 + yenirenk2);

			for (int r = 0; r < resim.getHeight(); r++) {
				for (int c = 0; c < resim.getWidth(); c++) {
					if (((int) dizikaSayisi.get(sayac)) == dizika[r][c])// dizika
						// global
						// yap.
						resim.setPixel(c, r,
								Color.rgb(yenirenk, yenirenk1, yenirenk2));
				}
			}
			sayac++;
		}
		return resim;
	}
}
