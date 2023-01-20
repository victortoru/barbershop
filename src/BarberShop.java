import java.util.concurrent.Semaphore;

public class BarberShop {
    static int sillas = 5; // número de sillas de espera
    static Semaphore barbero = new Semaphore(0); // barbero
    static Semaphore cliente = new Semaphore(0); // cliente
    static Semaphore sentarse = new Semaphore(1); // acceso a las sillas

    static Semaphore dormir = new Semaphore();

    static class Barbero implements Runnable {
        public void run() {
            while (true) {
                try {
                    cliente.acquire(); // el barbero espera a un cliente
                    sentarse.acquire(); // accede a las sillas
                    sillas++; // una silla se libera
                    System.out.println("El barbero está cortando el pelo, hay estas sillas disponibles " + chairs);
                    sentarse.release();
                    barbero.release(); // el barbero comienza a cortar el cabello
                } catch (InterruptedException e) { }
            }
        }
    }

    static class Cliente implements Runnable {
        public void run() {
            while (true) {
                try {
                    sentarse.acquire(); // accede a las sillas
                    if (sillas > 0) {
                        sillas--; // se sienta en una silla
                        System.out.println("El cliente se sienta y hay estas sillas disponibles: " + chairs);
                        cliente.release(); // despierta al barbero
                        sentarse.release();
                        barbero.acquire(); // espera a que el barbero termine
                    } else {
                        System.out.println("El cliente se va y quedan estas sillas disponibles " + chairs);
                        sentarse.release(); // no hay sillas disponibles
                    }
                } catch (InterruptedException e) { }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Barbero()).start();
        new Thread(new Cliente()).start();
        new Thread(new Cliente()).start();
        // puedes crear más hilos de clientes si lo deseas
    }
}