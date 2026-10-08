package bus;

public final class SalaryCalculator {
    public static final double MONTHLY_BASE = 5_000_000;
    public static final int STANDARD_WORK_DAYS = 22;

    private SalaryCalculator() {
    }

    public static Salary calculate(double salaryCoefficient, double positionAllowance, int workDays) {
        if (!Double.isFinite(salaryCoefficient) || salaryCoefficient <= 0) {
            throw new IllegalArgumentException("Hệ số lương phải là số dương hợp lệ.");
        }
        if (!Double.isFinite(positionAllowance) || positionAllowance < 0) {
            throw new IllegalArgumentException("Phụ cấp phải là số không âm hợp lệ.");
        }
        if (workDays < 0) {
            throw new IllegalArgumentException("Số ngày công không được âm.");
        }

        double baseSalary = MONTHLY_BASE * salaryCoefficient / STANDARD_WORK_DAYS * workDays;
        double proratedAllowance = positionAllowance
                * Math.min(workDays, STANDARD_WORK_DAYS) / STANDARD_WORK_DAYS;
        return new Salary(baseSalary, proratedAllowance, baseSalary + proratedAllowance);
    }

    public static final class Salary {
        private final double baseSalary;
        private final double allowance;
        private final double netPay;

        private Salary(double baseSalary, double allowance, double netPay) {
            this.baseSalary = baseSalary;
            this.allowance = allowance;
            this.netPay = netPay;
        }

        public double getBaseSalary() {
            return baseSalary;
        }

        public double getAllowance() {
            return allowance;
        }

        public double getNetPay() {
            return netPay;
        }
    }
}
