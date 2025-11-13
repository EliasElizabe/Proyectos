from playwright.sync_api import sync_playwright
import csv
import time
import random
import json
import os
from typing import Dict, Any

# ===== CONFIGURACIÓN MEJORADA =====
USER_AGENTS = [
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36",
]

# Configuración de tiempos optimizados
DELAY_RANGE = (10, 25)  # Reducido para mayor eficiencia
MAX_RETRIES = 3
RETRY_DELAYS = [20, 40, 80]  # Tiempos de espera más realistas
MAX_PAGES_PER_SESSION = random.randint(4, 6)  # Rango más ajustado

# Configuración de rutas
USER_DATA_DIR = os.path.expanduser(r"~\AppData\Local\Google\Chrome\User Data")  # Ruta automática
STORAGE_STATE = "google_auth.json"

# ===== FUNCIONES OPTIMIZADAS =====
def setup_browser(playwright):
    """Configuración ultra-optimizada para Chrome con manejo de errores robusto"""
    # 1. Configuración de directorios especial para Playwright
    PLAYWRIGHT_DATA_DIR = os.path.join(os.getcwd(), "playwright_data")
    os.makedirs(PLAYWRIGHT_DATA_DIR, exist_ok=True)
    
    # 2. Configuración mínima esencial
    context = playwright.chromium.launch_persistent_context(
        user_data_dir=PLAYWRIGHT_DATA_DIR,
        channel="chrome",
        executable_path=r"C:\Program Files\Google\Chrome\Application\chrome.exe",
        headless=False,
        timeout=180000,  # 3 minutos de timeout
        args=[
            '--start-maximized',
            '--disable-blink-features=AutomationControlled',
            '--disable-sync',
            '--disable-notifications',
            '--no-default-browser-check',
            '--no-first-run'
        ],
        ignore_default_args=[
            "--enable-automation",
            "--disable-extensions",
            "--disable-component-update",
            "--disable-background-networking"
        ],
        viewport={"width": 1366, "height": 768},
        locale="es-AR",
        timezone_id="America/Argentina/Buenos_Aires"
    )

    # 3. Inyección avanzada anti-detección
    context.add_init_script("""
        Object.defineProperty(navigator, 'webdriver', {
            get: () => undefined,
            configurable: true
        });
        window.navigator.chrome = {
            runtime: {},
            app: {
                isInstalled: false
            },
            webstore: {
                onInstallStageChanged: {},
                onDownloadProgress: {}
            },
            csi: function(){},
            loadTimes: function(){}
        };
        Object.defineProperty(navigator, 'plugins', {
            get: () => [1, 2, 3],
            configurable: true
        });
        Object.defineProperty(navigator, 'languages', {
            get: () => ['es-AR', 'es', 'en-US', 'en'],
            configurable: true
        });
    """)
    
    return context
    

def human_like_delay(min_sec: float = 1.5, max_sec: float = 4) -> None:
    """Espera tiempos aleatorios más humanos con variabilidad"""
    base_delay = random.uniform(min_sec, max_sec)
    # Añade micro-pausas para mayor realismo
    if random.random() > 0.6:
        time.sleep(base_delay * 0.3)
        time.sleep(base_delay * 0.7)
    else:
        time.sleep(base_delay)

def human_like_navigation(page, url: str) -> None:
    """Navegación mejorada que simula comportamiento humano"""
    try:
        # 1. Visita una página neutral con probabilidad ajustada
        if random.random() > 0.3:
            neutral_pages = [
                "https://www.google.com/search?q=inmuebles",
                "https://www.bing.com/search?q=propiedades",
                "https://duckduckgo.com/?q=casas+en+venta"
            ]
            page.goto(random.choice(neutral_pages), timeout=60000)
            human_like_delay(3, 6)
            
            # 2. Simula interacción con la página
            if random.random() > 0.4:
                page.mouse.move(
                    random.randint(100, 500),
                    random.randint(100, 300),
                    steps=random.randint(5, 15)
                )
                human_like_delay(0.5, 1.5)
                
                # Scroll con variabilidad
                for _ in range(random.randint(1, 3)):
                    page.mouse.wheel(0, random.randint(200, 600))
                    human_like_delay(0.8, 1.8)
        
        # 3. Navegación a la URL objetivo con manejo de errores
        response = page.goto(url, timeout=60000, wait_until="domcontentloaded")
        if not response or not response.ok:
            raise Exception(f"Error al cargar la página: {response.status if response else 'No response'}")
        
        human_like_delay(5, 12)
        
        # 4. Scroll humano mejorado
        scroll_steps = random.randint(2, 5)
        scroll_distance = page.viewport_size['height'] * 0.7
        for i in range(scroll_steps):
            # Alterna dirección ocasionalmente
            direction = -1 if i % 3 == 0 else 1
            page.mouse.wheel(0, scroll_distance * direction * random.uniform(0.8, 1.2))
            human_like_delay(1.5, 3.5)
    
    except Exception as e:
        print(f"⚠️ Error en navegación: {str(e)}")
        raise

def extract_property_data(page) -> Dict[str, Any]:
    """Extracción de datos mejorada con selectores robustos"""
    data = {
        "url": page.url,
        "titulo": "",
        "precio": "",
        "moneda": "",
        "ubicación": "",
        "descripción": "",
        "características": [],
        "fecha_publicación": "",
        "metros_cuadrados": "",
        "ambientes": ""
    }
    
    try:
        # Extracción con múltiples selectores de respaldo
        title_selectors = [
            "h1.sc-iqcoie-0",  # Selector principal
            "h1.listing-title",  # Selector alternativo
            "h1"  # Selector genérico
        ]
        data["titulo"] = page.query_selector_first_with_fallback(title_selectors) or ""
        
        # Extracción de precio mejorada
        price_text = page.query_selector_text("div.sc-iqcoie-2, div.price, span.price-value")
        if price_text:
            if "$" in price_text:
                data["moneda"] = "$"
                data["precio"] = ''.join(c for c in price_text if c.isdigit())
            elif "USD" in price_text:
                data["moneda"] = "USD"
                data["precio"] = ''.join(c for c in price_text if c.isdigit())
        
        # Extracción de ubicación
        data["ubicación"] = page.query_selector_text("div.sc-ge2uzh-0, div.location, address") or ""
        
        # Extracción de características con limpieza
        features = page.query_selector_all("div.sc-7uqkcd-0, div.features li, div.amenities span")
        for feature in features:
            feature_text = feature.text_content().strip()
            if "m²" in feature_text:
                data["metros_cuadrados"] = ''.join(c for c in feature_text if c.isdigit())
            elif "ambiente" in feature_text.lower():
                data["ambientes"] = feature_text.split()[0]
            else:
                data["características"].append(feature_text)
        
        # Unir características en string
        data["características"] = ", ".join(data["características"])
        
        # Extracción de descripción
        data["descripción"] = page.query_selector_text("div.sc-i1odl-5, div.description, p.long-desc") or ""
        
        # Extracción de fecha
        data["fecha_publicación"] = page.query_selector_text("div.sc-1uhtbxc-1, time.date, span.publish-date") or ""
    
    except Exception as e:
        print(f"⚠️ Error extrayendo datos: {str(e)[:200]}")
    
    return data

def handle_captcha(page) -> bool:
    """Manejo mejorado de CAPTCHAs"""
    captcha_selectors = [
        "iframe[src*='recaptcha']",
        "div[class*='captcha']",
        "text=Verificación de seguridad",
        "text=CAPTCHA",
        "text=robot"
    ]
    
    for selector in captcha_selectors:
        if page.locator(selector).count() > 0:
            print("⚠️ CAPTCHA detectado, requiriendo intervención manual...")
            try:
                page.screenshot(path="captcha_screenshot.png")
                print("📸 Captura de pantalla guardada como captcha_screenshot.png")
            except:
                pass
            page.pause()
            return True
    return False

def human_like_behavior(page) -> None:
    """Comportamiento humano mejorado con movimientos más realistas"""
    # Movimientos de mouse con trayectorias curvas
    for _ in range(random.randint(2, 5)):
        start_x = random.randint(0, page.viewport_size['width']//2)
        start_y = random.randint(0, page.viewport_size['height']//2)
        end_x = random.randint(0, page.viewport_size['width']//2)
        end_y = random.randint(0, page.viewport_size['height']//2)
        
        # Simula movimiento curvilíneo
        steps = random.randint(8, 15)
        for i in range(steps):
            t = i/steps
            # Curva Bézier simple
            x = start_x + (end_x - start_x) * t
            y = start_y + (end_y - start_y) * (t + random.uniform(-0.2, 0.2))
            page.mouse.move(x, y)
            human_like_delay(0.05, 0.15)
        
        # Pausa después del movimiento
        human_like_delay(0.3, 1.2)
        
        # Click aleatorio con probabilidad
        if random.random() > 0.7:
            page.mouse.click(x, y, delay=random.randint(100, 300))
            human_like_delay(0.5, 1.5)

# ===== FUNCIONES AUXILIARES NUEVAS =====
def query_selector_text(page, selectors: str) -> str:
    """Busca texto en múltiples selectores (primero que coincida)"""
    if isinstance(selectors, str):
        selectors = [selectors]
    
    for selector in selectors:
        element = page.locator(selector).first
        if element.count() > 0:
            return element.text_content().strip()
    return ""

def query_selector_all(page, selectors: str):
    """Devuelve todos los elementos que coinciden con los selectores"""
    if isinstance(selectors, str):
        selectors = [selectors]
    
    results = []
    for selector in selectors:
        elements = page.locator(selector).all()
        results.extend(elements)
    return results

# ===== PROGRAMA PRINCIPAL OPTIMIZADO =====
def main():
    # Leer URLs con manejo mejorado de errores
    try:
        with open("urls.txt", "r", encoding="utf-8") as f:
            urls = [line.strip() for line in f if line.strip()]
    except FileNotFoundError:
        print("❌ No se encontró el archivo urls.txt")
        return
    except Exception as e:
        print(f"❌ Error leyendo URLs: {str(e)}")
        return

    if not urls:
        print("❌ No hay URLs para procesar")
        return

    # Configurar archivo de salida con backup automático
    output_file = "output.csv"
    backup_file = f"output_backup_{int(time.time())}.csv"
    
    try:
        # Crear backup si ya existe el archivo
        if os.path.exists(output_file):
            import shutil
            shutil.copy2(output_file, backup_file)
            print(f"🔁 Creando backup: {backup_file}")

        with open(output_file, "a", newline="", encoding="utf-8") as csvfile:
            fieldnames = [
                "url", "titulo", "precio", "moneda", "ubicación", 
                "descripción", "características", "fecha_publicación",
                "metros_cuadrados", "ambientes"
            ]
            
            # Verificar si el archivo está vacío para escribir encabezados
            csvfile.seek(0, 2)  # Ir al final del archivo
            if csvfile.tell() == 0:
                writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
                writer.writeheader()
            else:
                writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

            with sync_playwright() as playwright:
                try:
                    context = setup_browser(playwright)
                    page = context.new_page()
                    
                    # Manejo de sesión mejorado
                    if not os.path.exists(STORAGE_STATE):
                        print("🔑 Por favor inicia sesión manualmente en Google")
                        page.goto("https://accounts.google.com", timeout=90000)
                        input("⏳ Presiona Enter cuando hayas terminado el login...")
                        context.storage_state(path=STORAGE_STATE)
                        print(f"✅ Estado de sesión guardado en {STORAGE_STATE}")
                    else:
                        # Cargar estado de sesión existente
                        with open(STORAGE_STATE) as f:
                            storage_state = json.load(f)
                        context.add_cookies(storage_state['cookies'])
                    
                    success_count = 0
                    total_urls = len(urls)
                    session_count = 0

                    print(f"\n🚀 Iniciando scraping de {total_urls} propiedades...\n")

                    for i, url in enumerate(urls, 1):
                        print(f"[{i}/{total_urls}] Procesando: {url[:70]}...")
                        
                        for attempt in range(MAX_RETRIES):
                            try:
                                human_like_navigation(page, url)
                                human_like_behavior(page)
                                
                                # Verificación de carga de página mejorada
                                if not page.locator("body").count() > 0:
                                    raise Exception("Página no cargada correctamente")
                                
                                # Manejo de CAPTCHA
                                if handle_captcha(page):
                                    print("🔄 Reintentando después de CAPTCHA...")
                                    continue
                                
                                # Rotación de sesión
                                if session_count >= MAX_PAGES_PER_SESSION:
                                    print("🔄 Rotando sesión...")
                                    try:
                                        context.storage_state(path=STORAGE_STATE)
                                        context.close()
                                    except:
                                        pass
                                    context = setup_browser(playwright)
                                    page = context.new_page()
                                    session_count = 0
                                    human_like_delay(20, 40)
                                
                                # Extracción de datos
                                data = extract_property_data(page)
                                if data["titulo"]:  # Validación básica de datos
                                    writer.writerow(data)
                                    csvfile.flush()  # Guardar después de cada escritura
                                    success_count += 1
                                    session_count += 1
                                    print(f"✅ Éxito (Intento {attempt + 1})")
                                    break
                                else:
                                    raise Exception("No se extrajeron datos válidos")
                            
                            except Exception as e:
                                print(f"⚠️ Intento {attempt + 1} fallido: {str(e)[:150]}")
                                if attempt < MAX_RETRIES - 1:
                                    wait_time = RETRY_DELAYS[attempt] * (1 + random.random())
                                    print(f"⏳ Esperando {wait_time:.1f}s antes de reintentar...")
                                    time.sleep(wait_time)
                                    
                                    # Limpieza antes de reintentar
                                    try:
                                        context.clear_cookies()
                                        page.goto("about:blank")
                                        human_like_delay(3, 7)
                                    except:
                                        pass
                                else:
                                    print(f"❌ Fallo después de {MAX_RETRIES} intentos")
                        
                        # Pausa entre URLs
                        delay = random.uniform(*DELAY_RANGE)
                        print(f"⏳ Esperando {delay:.1f}s antes de la siguiente URL...")
                        time.sleep(delay)

                    # Guardado final del estado
                    try:
                        context.storage_state(path=STORAGE_STATE)
                        context.close()
                    except:
                        pass
                    
                    print(f"\n🎉 Proceso completado. {success_count}/{total_urls} propiedades obtenidas.")
                    print(f"💾 Resultados guardados en {output_file}")
                    if success_count < total_urls:
                        print("ℹ️ Algunas propiedades no pudieron ser obtenidas. Revisa los logs.")
                
                except Exception as e:
                    print(f"❌ Error crítico durante el scraping: {str(e)}")
                    try:
                        context.close()
                    except:
                        pass
                    raise
    
    except Exception as e:
        print(f"❌ Error durante la ejecución: {str(e)}")
    finally:
        # Limpieza final opcional
        pass

if __name__ == "__main__":
    main()
