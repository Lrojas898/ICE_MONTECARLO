module MontCarloPiEstimation {
    interface Worker {
        int calculatePoints(int totalPoints);
    }

    interface Master {
        float estimatePi(int totalPoints, int workers);
    }
}
