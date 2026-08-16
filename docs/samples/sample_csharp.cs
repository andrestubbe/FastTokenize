// FastTokenizer Spectrum Test Sample: C#
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FastJava.TokenizerSample
{
    public interface ITokenProcessor
    {
        Task<int> ProcessAsync(string input);
    }

    /// <summary>
    /// C# Spectrum Test Class
    /// </summary>
    [Serializable]
    public class CSharpSample : ITokenProcessor
    {
        public string Version { get; set; } = "1.0.0-rc1";
        private readonly int _maxRetries = 3;

        public async Task<int> ProcessAsync(string input)
        {
            if (string.IsNullOrEmpty(input))
                throw new ArgumentNullException(nameof(input));

            // Interpolated String & Verbatim String
            string formatted = $"Processing '{input}' with version {Version} @ {DateTime.UtcNow}";
            string path = @"C:\Users\FastJava\Projects\FastTokenizer";

            int resultCount = 0;
            await Task.Run(() =>
            {
                var list = new List<string> { "one", "two", "three" };
                foreach (var item in list)
                {
                    resultCount += item.Length;
                }
            });

            return resultCount;
        }
    }
}
