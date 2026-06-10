const Register = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-6">
      <div className="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md">
        <h2 className="text-3xl font-bold text-center mb-4">Create Account</h2>
        <p className="text-center text-gray-600 mb-8">
          Registration UI is coming next. For now, create test users through the backend API.
        </p>
        <a
          href="/login"
          className="block w-full bg-orange-600 text-white py-3 rounded-lg font-semibold text-center hover:bg-orange-700"
        >
          Back to Login
        </a>
      </div>
    </div>
  );
};

export default Register;
