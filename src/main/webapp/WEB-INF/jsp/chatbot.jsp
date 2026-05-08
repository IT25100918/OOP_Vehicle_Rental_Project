<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  chatbot.jsp — Elite Wheel Rentals AI Assistant
  Include this fragment just before </body> on any page.
  Visible to all visitors — no login required.
  FAQ answers run client-side; unknown questions fall back to Claude API.
--%>
<style>
/* ── Chatbot bubble & window ───────────────────────── */
#ew-chat-bubble {
  position: fixed; bottom: 28px; right: 28px; z-index: 9999;
  width: 56px; height: 56px; border-radius: 50%;
  background: linear-gradient(135deg, #D4A843, #E8B84B);
  border: none; cursor: pointer; box-shadow: 0 4px 20px rgba(212,168,67,0.5);
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; transition: transform 0.2s, box-shadow 0.2s;
}
#ew-chat-bubble:hover { transform: scale(1.1); box-shadow: 0 6px 28px rgba(212,168,67,0.7); }
#ew-chat-bubble .notif-dot {
  position: absolute; top: 4px; right: 4px;
  width: 10px; height: 10px; background: #ef4444; border-radius: 50%;
  border: 2px solid #0D0D0D; animation: pulse-dot 2s infinite;
}
@keyframes pulse-dot { 0%,100%{transform:scale(1)} 50%{transform:scale(1.4)} }

#ew-chat-window {
  position: fixed; bottom: 96px; right: 28px; z-index: 9999;
  width: 360px; max-height: 520px;
  background: #111827; border: 1px solid rgba(212,168,67,0.25);
  border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.6);
  display: none; flex-direction: column; overflow: hidden;
  animation: slideUp 0.25s ease;
}
@keyframes slideUp { from{opacity:0;transform:translateY(20px)} to{opacity:1;transform:translateY(0)} }

/* Header */
#ew-chat-header {
  background: linear-gradient(135deg, #1a2744, #0f1628);
  border-bottom: 1px solid rgba(212,168,67,0.2);
  padding: 14px 16px; display: flex; align-items: center; gap: 10px;
}
.chat-avatar {
  width: 38px; height: 38px; border-radius: 50%;
  background: linear-gradient(135deg,#D4A843,#E8B84B);
  display: flex; align-items: center; justify-content: center;
  font-size: 18px; flex-shrink: 0;
}
.chat-header-info { flex: 1; }
.chat-header-name { font-weight: 700; font-size: 14px; color: #fff; font-family: 'Rajdhani',sans-serif; }
.chat-header-status { font-size: 11px; color: #4ade80; display: flex; align-items: center; gap: 4px; }
.chat-header-status::before { content:''; width:6px; height:6px; background:#4ade80; border-radius:50%; display:inline-block; }
#ew-chat-close { background:none; border:none; color:#8A8A8A; font-size:18px; cursor:pointer; padding:4px; line-height:1; }
#ew-chat-close:hover { color:#fff; }

/* Messages */
#ew-chat-messages {
  flex: 1; overflow-y: auto; padding: 16px; display: flex;
  flex-direction: column; gap: 10px; min-height: 0; max-height: 340px;
}
#ew-chat-messages::-webkit-scrollbar { width: 4px; }
#ew-chat-messages::-webkit-scrollbar-track { background: transparent; }
#ew-chat-messages::-webkit-scrollbar-thumb { background: rgba(212,168,67,0.3); border-radius: 4px; }

.chat-msg { display: flex; gap: 8px; max-width: 90%; }
.chat-msg.user { align-self: flex-end; flex-direction: row-reverse; }
.chat-msg.bot  { align-self: flex-start; }
.msg-avatar { width:28px; height:28px; border-radius:50%; flex-shrink:0; display:flex; align-items:center; justify-content:center; font-size:13px; }
.bot .msg-avatar  { background: linear-gradient(135deg,#D4A843,#E8B84B); }
.user .msg-avatar { background: rgba(255,255,255,0.1); }
.msg-bubble {
  padding: 10px 13px; border-radius: 14px; font-size: 13px; line-height: 1.55; color: #e5e7eb;
}
.bot  .msg-bubble { background: #1f2937; border-bottom-left-radius: 4px; }
.user .msg-bubble { background: linear-gradient(135deg,#D4A843,#c49430); color:#111; border-bottom-right-radius: 4px; font-weight:500; }

.typing-indicator { display:flex; gap:5px; align-items:center; padding:10px 13px; background:#1f2937; border-radius:14px; border-bottom-left-radius:4px; }
.typing-indicator span { width:7px; height:7px; background:#D4A843; border-radius:50%; animation:typing 1.2s infinite; }
.typing-indicator span:nth-child(2){animation-delay:.2s}
.typing-indicator span:nth-child(3){animation-delay:.4s}
@keyframes typing{0%,60%,100%{transform:translateY(0)}30%{transform:translateY(-6px)}}

/* Quick replies */
#ew-quick-replies { padding: 8px 12px; display: flex; flex-wrap: wrap; gap: 6px; border-top: 1px solid rgba(255,255,255,0.06); }
.quick-btn {
  background: rgba(212,168,67,0.1); border: 1px solid rgba(212,168,67,0.3);
  color: #D4A843; padding: 5px 11px; border-radius: 999px; font-size: 11px;
  cursor: pointer; transition: all 0.15s; white-space: nowrap;
}
.quick-btn:hover { background: rgba(212,168,67,0.25); }

/* Input */
#ew-chat-input-row {
  padding: 12px; border-top: 1px solid rgba(255,255,255,0.07);
  display: flex; gap: 8px; align-items: center;
}
#ew-chat-input {
  flex: 1; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.12);
  border-radius: 10px; padding: 9px 13px; font-size: 13px; color: #fff;
  outline: none; font-family: inherit; resize: none;
  transition: border-color 0.2s;
}
#ew-chat-input:focus { border-color: rgba(212,168,67,0.5); }
#ew-chat-input::placeholder { color: #6b7280; }
#ew-chat-send {
  width: 36px; height: 36px; border-radius: 10px;
  background: linear-gradient(135deg,#D4A843,#E8B84B);
  border: none; cursor: pointer; display: flex; align-items: center; justify-content: center;
  font-size: 15px; flex-shrink: 0; transition: opacity 0.2s;
}
#ew-chat-send:hover { opacity: 0.85; }
#ew-chat-send:disabled { opacity: 0.4; cursor: not-allowed; }
</style>

<!-- Bubble button -->
<button id="ew-chat-bubble" onclick="ewToggleChat()" title="Chat with EW Assistant">
  🚗
  <div class="notif-dot"></div>
</button>

<!-- Chat window -->
<div id="ew-chat-window">

  <!-- Header -->
  <div id="ew-chat-header">
    <div class="chat-avatar">🚗</div>
    <div class="chat-header-info">
      <div class="chat-header-name">EW Assistant</div>
      <div class="chat-header-status">Online — Elite Wheel Rentals</div>
    </div>
    <button id="ew-chat-close" onclick="ewToggleChat()">✕</button>
  </div>

  <!-- Messages -->
  <div id="ew-chat-messages"></div>

  <!-- Quick replies -->
  <div id="ew-quick-replies">
    <button class="quick-btn" onclick="ewQuick('How do I book a vehicle?')">How to book?</button>
    <button class="quick-btn" onclick="ewQuick('What vehicles are available?')">Vehicles?</button>
    <button class="quick-btn" onclick="ewQuick('How do I cancel a booking?')">Cancel booking?</button>
    <button class="quick-btn" onclick="ewQuick('How does payment work?')">Payments?</button>
    <button class="quick-btn" onclick="ewQuick('What is the late fee policy?')">Late fees?</button>
  </div>

  <!-- Input -->
  <div id="ew-chat-input-row">
    <input id="ew-chat-input" type="text" placeholder="Ask anything about rentals..."
           onkeydown="if(event.key==='Enter')ewSend()">
    <button id="ew-chat-send" onclick="ewSend()">➤</button>
  </div>

</div>

<script>
(function() {
  // ── State ────────────────────────────────────────────
  let isOpen = false;
  let isTyping = false;
  const history = []; // {role, content} for AI context

  // ── FAQ knowledge base ───────────────────────────────
  const FAQ = [
    {
      patterns: ['how.*book', 'booking.*process', 'rent.*vehicle', 'how to rent', 'make.*booking'],
      answer: '📋 <b>How to book:</b><br>1. Go to <b>Vehicles</b> and browse the fleet<br>2. Click <b>Book Now</b> on any available vehicle<br>3. Select your start & end dates<br>4. Confirm — your booking is instant!<br><br>You can view all bookings under <b>My Bookings</b>.'
    },
    {
      patterns: ['cancel', 'cancellation', 'cancel.*booking', 'how.*cancel'],
      answer: '❌ <b>Cancelling a booking:</b><br>Go to <b>My Bookings</b>, find the booking with status <i>Confirmed</i>, and click <b>Cancel</b>.<br><br>Only confirmed bookings can be cancelled. Completed bookings cannot be cancelled.'
    },
    {
      patterns: ['pay', 'payment', 'how.*pay', 'payment method', 'cash', 'online.*pay'],
      answer: '💳 <b>Payment options:</b><br>We accept two methods:<br>• <b>Cash</b> — pay in person, no extra fee<br>• <b>Online Transfer</b> — 2% processing fee applies<br><br>After your booking is marked <i>Completed</i>, click <b>Pay</b> to process payment.'
    },
    {
      patterns: ['late fee', 'overdue', 'late return', 'penalty', 'extra charge'],
      answer: '⚠️ <b>Late fee policy:</b><br>If you return the vehicle after the agreed end date, a late fee is automatically calculated at <b>20% of the daily rate per overdue day</b>.<br><br>You\'ll see late fees highlighted in red on the payment page.'
    },
    {
      patterns: ['vehicle', 'available', 'fleet', 'what.*car', 'what.*bike', 'what.*van', 'types'],
      answer: '🚗 <b>Our fleet includes:</b><br>• Cars, SUVs, Vans<br>• Bikes & Three-Wheelers<br>• Buses & Lorries<br><br>Go to <b>Vehicles</b> to browse the full fleet, filter by type, and sort by price or availability.'
    },
    {
      patterns: ['price', 'cost', 'how much', 'rate', 'charges', 'fee', 'rs\\.?', 'rupee'],
      answer: '💰 <b>Pricing:</b><br>Each vehicle shows its <b>Rs./day</b> rate on the fleet page. When booking, the total cost is calculated automatically based on your selected dates.<br><br>Prices vary by vehicle type — use the <b>Sort by Price</b> filter to find budget options.'
    },
    {
      patterns: ['profile', 'account', 'change.*password', 'update.*detail', 'edit.*profile'],
      answer: '👤 <b>Your profile:</b><br>Go to <b>My Profile</b> from the dashboard to update your name, email, contact, and password.<br><br>Your NIC and user ID cannot be changed after registration.'
    },
    {
      patterns: ['review', 'feedback', 'rating', 'leave.*review', 'write.*review'],
      answer: '⭐ <b>Leaving a review:</b><br>Go to <b>Reviews</b> and click <b>Write Review</b>. Select the vehicle, give a star rating (1–5), choose Vehicle or Service review, and submit.<br><br>You can edit your own reviews anytime.'
    },
    {
      patterns: ['register', 'sign up', 'create.*account', 'new.*account'],
      answer: '📝 <b>Registration:</b><br>Click <b>Register</b> on the home page to create a new account. You\'ll need your name, NIC, contact number, email, and driving licence number.<br><br>Already have an account? Just <b>Login</b> to get started!'
    },
    {
      patterns: ['contact', 'support', 'help', 'customer service', 'phone', 'email.*us'],
      answer: '📞 <b>Support:</b><br>For direct assistance, please contact our team through the front desk or visit us in person.<br><br>For platform issues, you can also leave feedback through the <b>Reviews</b> section.'
    },
    {
      patterns: ['hello', 'hi', 'hey', 'good morning', 'good afternoon', 'greet'],
      answer: '👋 Hello! I\'m the <b>EW Assistant</b> for Elite Wheel Rentals.<br>I can help you with bookings, payments, vehicles, late fees, and more.<br><br>What would you like to know?'
    },
    {
      patterns: ['thank', 'thanks', 'thank you', 'ty'],
      answer: '😊 You\'re welcome! Is there anything else I can help you with?'
    }
  ];

  // ── FAQ matcher ──────────────────────────────────────
  function matchFAQ(text) {
    const lower = text.toLowerCase();
    for (const entry of FAQ) {
      for (const pattern of entry.patterns) {
        if (new RegExp(pattern, 'i').test(lower)) {
          return entry.answer;
        }
      }
    }
    return null;
  }

  // ── UI helpers ───────────────────────────────────────
  window.ewToggleChat = function() {
    isOpen = !isOpen;
    const win = document.getElementById('ew-chat-window');
    win.style.display = isOpen ? 'flex' : 'none';
    // Remove notif dot on first open
    const dot = document.querySelector('#ew-chat-bubble .notif-dot');
    if (dot) dot.remove();
    // Show welcome on first open
    if (isOpen && document.getElementById('ew-chat-messages').children.length === 0) {
      const rawName = '${user.name}';
      const name = rawName && rawName !== '' ? rawName.split(' ')[0] : null;
      const greeting = name
        ? '👋 Hi <b>' + name + '</b>! I\'m the EW Assistant.'
        : '👋 Hi there! I\'m the <b>EW Assistant</b>.';
      addBotMsg(greeting + '<br>I can answer questions about bookings, vehicles, payments, and more.<br><br>Use the quick buttons below or type your question!');
    }
    if (isOpen) {
      setTimeout(() => document.getElementById('ew-chat-input').focus(), 100);
    }
  };

  window.ewQuick = function(text) {
    document.getElementById('ew-chat-input').value = text;
    ewSend();
  };

  function addUserMsg(text) {
    const el = document.createElement('div');
    el.className = 'chat-msg user';
    el.innerHTML = '<div class="msg-avatar">👤</div><div class="msg-bubble">' + escHtml(text) + '</div>';
    msgs().appendChild(el);
    scrollBottom();
  }

  function addBotMsg(html) {
    const el = document.createElement('div');
    el.className = 'chat-msg bot';
    el.innerHTML = '<div class="msg-avatar">🚗</div><div class="msg-bubble">' + html + '</div>';
    msgs().appendChild(el);
    scrollBottom();
  }

  function showTyping() {
    const el = document.createElement('div');
    el.className = 'chat-msg bot'; el.id = 'ew-typing';
    el.innerHTML = '<div class="msg-avatar">🚗</div><div class="typing-indicator"><span></span><span></span><span></span></div>';
    msgs().appendChild(el); scrollBottom();
  }

  function hideTyping() {
    const el = document.getElementById('ew-typing');
    if (el) el.remove();
  }

  function msgs() { return document.getElementById('ew-chat-messages'); }
  function scrollBottom() { const m = msgs(); m.scrollTop = m.scrollHeight; }
  function escHtml(t) { return t.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;'); }

  // ── Send message ─────────────────────────────────────
  window.ewSend = async function() {
    const input = document.getElementById('ew-chat-input');
    const text = input.value.trim();
    if (!text || isTyping) return;

    input.value = '';
    addUserMsg(text);
    history.push({ role: 'user', content: text });

    // 1. Try FAQ first
    const faqAnswer = matchFAQ(text);
    if (faqAnswer) {
      isTyping = true;
      showTyping();
      await sleep(600); // small delay feels natural
      hideTyping();
      addBotMsg(faqAnswer);
      history.push({ role: 'assistant', content: faqAnswer.replace(/<[^>]+>/g,'') });
      isTyping = false;
      return;
    }

    // 2. Fall back to Claude AI
    isTyping = true;
    document.getElementById('ew-chat-send').disabled = true;
    showTyping();

    try {
      const systemPrompt = `You are EW Assistant, a friendly and helpful chatbot for Elite Wheel Rentals — a vehicle rental platform in Sri Lanka. 
You help users with: booking vehicles, understanding payments, late fees, cancellations, account profiles, reviews, and general rental questions.
The platform has cars, SUVs, vans, bikes, three-wheelers, buses, and lorries available for rent.
Payment is Rs. per day. Late fee is 20% of daily rate per overdue day. Payment methods: cash (no fee) or online (2% fee).
Keep answers concise, friendly, and relevant to vehicle rentals. Use simple formatting. Do not answer questions unrelated to the rental platform.`;

      const response = await fetch('https://api.anthropic.com/v1/messages', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          model: 'claude-sonnet-4-20250514',
          max_tokens: 300,
          system: systemPrompt,
          messages: history.slice(-10) // last 10 messages for context
        })
      });

      const data = await response.json();
      hideTyping();

      if (data.content && data.content[0] && data.content[0].text) {
        const reply = data.content[0].text;
        addBotMsg(escHtml(reply).replace(/\n/g, '<br>').replace(/\*\*(.*?)\*\*/g,'<b>$1</b>'));
        history.push({ role: 'assistant', content: reply });
      } else {
        addBotMsg('Sorry, I couldn\'t get a response right now. Please try asking again or use the quick reply buttons!');
      }
    } catch (err) {
      hideTyping();
      addBotMsg('⚠️ Connection issue. Please check your internet and try again.');
    }

    isTyping = false;
    document.getElementById('ew-chat-send').disabled = false;
  };

  function sleep(ms) { return new Promise(r => setTimeout(r, ms)); }
})();
</script>
