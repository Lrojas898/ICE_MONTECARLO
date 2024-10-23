module MontCarloPiEstimation {
    interface Worker {
        int calculatePoints(int totalPoints);
    }

    interface Master {
        float estimatePi(int totalPoints);
        void registerWorker(string name, Worker* worker);
        void unregisterWorker(string name);
    }
}
