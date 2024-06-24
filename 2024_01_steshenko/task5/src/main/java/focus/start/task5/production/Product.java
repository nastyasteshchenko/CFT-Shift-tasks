package focus.start.task5.production;

class Product {

    private static final IdGenerator ID_GENERATOR = new IdGenerator();
    private final long id = ID_GENERATOR.getId();

    long getId() {
        return id;
    }
}
