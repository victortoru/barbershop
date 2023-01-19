import java.util.concurrent.Semaphore;

public class BarberShop {
    static int chairs = 5; // número de sillas de espera
    static Semaphore barber = new Semaphore(0); // barbero
    static Semaphore customer = new Semaphore(0); // cliente
    static Semaphore accessSeats = new Semaphore(1); // acceso a las sillas

    static class Barber implements Runnable {
        public void run() {
            while (true) {
                try {
                    customer.acquire(); // el barbero espera a un cliente
                    accessSeats.acquire(); // accede a las sillas
                    chairs++; // una silla se libera
                    System.out.println("Barber: Cutting hair. Chairs available: " + chairs);
                    accessSeats.release();
                    barber.release(); // el barbero comienza a cortar el cabello
                } catch (InterruptedException e) { }
            }
        }
    }

    static class Customer implements Runnable {
        public void run() {
            while (true) {
                try {
                    accessSeats.acquire(); // accede a las sillas
                    if (chairs > 0) {
                        chairs--; // se sienta en una silla
                        System.out.println("Customer: Sitting. Chairs available: " + chairs);
                        customer.release(); // despierta al barbero
                        accessSeats.release();
                        barber.acquire(); // espera a que el barbero termine
                    } else {
                        System.out.println("Customer: Leaving. Chairs available: " + chairs);
                        accessSeats.release(); // no hay sillas disponibles
                    }
                } catch (InterruptedException e) { }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Barber()).start();
        new Thread(new Customer()).start();
        new Thread(new Customer()).start();
        // puedes crear más hilos de clientes si lo deseas
    }
}